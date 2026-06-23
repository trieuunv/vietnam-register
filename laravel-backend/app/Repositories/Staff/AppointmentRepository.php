<?php

namespace App\Repositories\Staff;

use App\Models\Appointment;
use App\Repositories\Core\BaseRepository;

class AppointmentRepository extends BaseRepository
{
    public function __construct(Appointment $model)
    {
        parent::__construct($model);
    }

    public function getAllInCenter($centerId, array $filter = [])
    {
        $query = $this->model->where('center_id', $centerId);

        if (!empty($filter['status'])) {
            $query->where('status', $filter['status']);
        }

        if (!empty($filter['date_from'])) {
            $query->whereDate('appointment_date', '>=', $filter['date_from']);
        }

        if (!empty($filter['date_to'])) {
            $query->whereDate('appointment_date', '<=', $filter['date_to']);
        }

        if (!empty($filter['owner_name'])) {
            $query->whereHas('owner.user', function ($subQuery) use ($filter) {
                $subQuery->where('name', 'like', '%' . $filter['owner_name'] . '%');
            });
        }

        if (!empty($filter['vehicle_reg_number'])) {
            $query->whereHas('vehicle', function ($subQuery) use ($filter) {
                $subQuery->where('registration_number', 'like', '%' . $filter['vehicle_reg_number'] . '%');
            });
        }

        $query->with(['vehicle', 'owner.user']);
        $query->orderByDesc('appointment_date');

        return $query->get();
    }

    public function findInCenter($centerId, $appointmentId)
    {
        return $this->model->where('id', $appointmentId)
            ->where('center_id', $centerId)
            ->with('vehicle', 'owner', 'vehicle.vehicleType', 'inspection')
            ->first();
    }
}
