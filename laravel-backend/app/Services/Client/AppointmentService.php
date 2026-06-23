<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Client\AppointmentRepository;
use App\Repositories\Client\OwnerRepository;
use App\Repositories\Client\VehicleRepository;
use Illuminate\Support\Str;
use App\Helpers\HolidayHelper;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;

class AppointmentService
{
    protected $appointmentRepository;
    protected $ownerRepository;
    protected $vehicleRepository;

    public function __construct(
        AppointmentRepository $appointmentRepository,
        OwnerRepository $ownerRepository,
        VehicleRepository $vehicleRepository
    ) {
        $this->appointmentRepository = $appointmentRepository;
        $this->ownerRepository = $ownerRepository;
        $this->vehicleRepository = $vehicleRepository;
    }

    public function create(User $user, array $data)
    {
        $appointmentDate = Carbon::parse($data['appointment_date']);
        if (HolidayHelper::isOffDay($appointmentDate)) {
            throw new HandlerException('Không thể đặt lịch hẹn vào ngày nghỉ hoặc ngày lễ.', 400);
        }

        $owner = $this->ownerRepository->findById($user->owner->id);
        $vehicle = $this->vehicleRepository->findWithOwner($owner->id, $data['vehicle_id']);

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        if ($this->appointmentRepository->hasActiveAppointment($vehicle->id)) {
            throw new HandlerException('Xe này đã có lịch hẹn đăng kiểm đang chờ xử lý hoặc đã được xác nhận.', 400);
        }

        $capacity = 30; // Giới hạn 30 lịch hẹn mỗi ngày cho một trung tâm
        $dateStr = $appointmentDate->toDateString();
        $bookedCount = $this->appointmentRepository->countActiveAppointmentsOnDate($data['center_id'], $dateStr);
        if ($bookedCount >= $capacity) {
            throw new HandlerException('Trung tâm đăng kiểm đã đạt giới hạn số lượt đặt lịch trong ngày này. Vui lòng chọn ngày khác.', 400);
        }

        $data['owner_id'] = $owner->id;
        $data['created_by'] = $user->id;
        $data['status'] = 'pending';
        $data['confirmation_code'] = Str::random(20);

        return DB::transaction(function () use ($data) {
            $appointment = $this->appointmentRepository->create($data);

            if (!$appointment) {
                throw new HandlerException('Cannot make appointment.', 500);
            }

            return $appointment;
        });
    }

    public function getByStatus(User $user, $status)
    {
        $owner = $this->ownerRepository->findById($user->owner->id);
        $appointments = $this->appointmentRepository->getByStatus($owner->id, $status);

        return $appointments;
    }

    public function getById(User $user, $appointmentId)
    {
        $appointment = $this->appointmentRepository->findById($appointmentId);

        if (!$appointment || $appointment->owner_id !== $user->owner->id) {
            throw new HandlerException('Appointment not found', 404);
        }

        return $appointment;
    }
}
