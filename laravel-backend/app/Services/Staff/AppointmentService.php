<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Common\NotificationRepository;
use App\Repositories\Staff\AppointmentRepository;
use App\Repositories\Staff\CenterRepository;
use App\Repositories\Staff\CriteriaResultRepository;
use App\Repositories\Staff\InspectionCriteriaRepository;
use App\Repositories\Staff\InspectionRepository;
use App\Repositories\Staff\OwnerRepository;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class AppointmentService
{
    protected $appointmentRepository;
    protected $notificationRepository;
    protected $ownerRepository;
    protected $inspectionRepository;
    protected $criteriaResultRepository;
    protected $inspectionCriteriaRepository;

    public function __construct(
        AppointmentRepository $appointmentRepository,
        NotificationRepository $notificationRepository,
        OwnerRepository $ownerRepository,
        InspectionRepository $inspectionRepository,
        CriteriaResultRepository $criteriaResultRepository,
        InspectionCriteriaRepository $inspectionCriteriaRepository
    ) {
        $this->appointmentRepository = $appointmentRepository;
        $this->notificationRepository = $notificationRepository;
        $this->ownerRepository = $ownerRepository;
        $this->inspectionRepository = $inspectionRepository;
        $this->criteriaResultRepository = $criteriaResultRepository;
        $this->inspectionCriteriaRepository = $inspectionCriteriaRepository;
    }

    public function getAll(User $staff, array $filter = [])
    {
        $center = $staff->center;

        if (!$center) {
            throw new HandlerException('You are not authorized to access this resource.', 403);
        }

        $appointments = $this->appointmentRepository->getAllInCenter($center->id, $filter);

        return $appointments;
    }

    public function getById(User $staff, $appointmentId)
    {
        $appointment = $this->appointmentRepository->findInCenter($staff->center->id, $appointmentId);

        if (!$appointment) {
            throw new HandlerException('Appointment not found.', 404);
        }

        return $appointment;
    }

    public function confirm($staff, $appointmentId)
    {
        $appointment = $this->appointmentRepository->findById($appointmentId);

        if (!$appointment) {
            throw new HandlerException('Appointment not found.', 404);
        }

        if (!$staff->center || $appointment->center_id !== $staff->center->id) {
            throw new HandlerException('You are not authorized to confirm this appointment.', 403);
        }

        return DB::transaction(function () use ($staff, $appointment) {
            $this->appointmentRepository->update($appointment, ['status' => 'confirmed']);

            $owner = $this->ownerRepository->findById($appointment->owner_id);

            $userClient = $owner->user;

            $notificationClient = [
                'user_id' => $userClient->id,
                'title' => 'Lịch hẹn đã được chấp thuận',
                'content' => 'Yêu cầu lịch hẹn kiểm định phương tiện của bạn đã được chấp thuận. Vui lòng kiểm tra chi tiết trong hệ thống.',
                'notification_type' => 'appointment',
                'related_id' => $appointment->id,
                'related_type' => 'Appointment'
            ];

            $this->notificationRepository->create($notificationClient);

            return $this->getById($staff, $appointment->id);
        });
    }

    public function reject($user, $appointmentId)
    {
        $appointment = $this->appointmentRepository->findById($appointmentId);

        if (!$appointment) {
            throw new HandlerException('Appointment not found.', 404);
        }

        if (!$user->center || $appointment->center_id !== $user->center->id) {
            throw new HandlerException('You are not authorized to reject this appointment.', 403);
        }

        $updated = $this->appointmentRepository->update($appointment, ['status' => 'cancelled']);

        $owner = $this->ownerRepository->findById($appointment->owner_id);
        $userClient = $owner->user;

        $notificationClient = [
            'user_id' => $userClient->id,
            'title' => 'Lịch hẹn bị từ chối',
            'content' => 'Yêu cầu lịch hẹn kiểm định phương tiện của bạn đã bị từ chối. Vui lòng kiểm tra chi tiết trong hệ thống hoặc đặt lại lịch hẹn mới.',
            'notification_type' => 'appointment',
            'related_id' => $appointment->id,
            'related_type' => 'Appointment'
        ];

        $this->notificationRepository->create($notificationClient);

        return $updated;
    }

    public function startInspection(User $staff, $appointmentId)
    {
        $appointment = $this->appointmentRepository->findById($appointmentId);

        $vehicle = $appointment->vehicle;

        if (!$appointment) {
            throw new HandlerException('Appointment not found.', 404);
        }

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        if (!$staff->center || $appointment->center_id !== $staff->center->id) {
            throw new HandlerException('You are not authorized to start this inspection.', 403);
        }

        if ($appointment->status !== 'confirmed') {
            throw new HandlerException('Appointment is not in a valid state to start inspection.', 400);
        }

        return DB::transaction(function () use ($appointment, $vehicle, $staff) {
            $inspection = $this->inspectionRepository->create([
                'vehicle_id' => $appointment->vehicle_id,
                'center_id' => $appointment->center_id,
                'inspector_id' => $staff->id,
                'inspection_date' => now(),
                'next_inspection_date' => Carbon::now()
                    ->addMonths($appointment->vehicle->vehicleType->inspection_frequency_months),
                'certificate_number' => Str::random(10),
                'fee' => $appointment->vehicle->vehicleType->inspection_fee,
                'status' => 'in_progress'
            ]);

            $this->appointmentRepository->update($appointment, [
                'inspection_id' => $inspection->id,
                'status' => 'completed'
            ]);

            $criteriaList = $this->inspectionCriteriaRepository->getByVehicleType($vehicle->vehicle_type_id);

            if (!$criteriaList || count($criteriaList) === 0) {
                throw new HandlerException('No inspection criteria found for this vehicle type.', 500);
            }

            foreach ($criteriaList as $criteria) {
                $this->criteriaResultRepository->create([
                    'inspection_id' => $inspection->id,
                    'criteria_id' => $criteria->id,
                    'inspector_id' => $staff->id,
                    'result' => 'uninspected',
                    'notes' => null,
                ]);
            }

            return $this->getById($staff, $appointment->id);
        });
    }
}
