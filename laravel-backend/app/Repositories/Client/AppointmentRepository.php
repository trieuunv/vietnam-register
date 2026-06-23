<?php

namespace App\Repositories\Client;

use App\Models\Appointment;
use App\Repositories\Core\BaseRepository;

class AppointmentRepository extends BaseRepository
{
    public function __construct(Appointment $model)
    {
        parent::__construct($model);
    }

    public function getByStatus($ownerId, $status)
    {
        $query = $this->model->where('owner_id', $ownerId)->with(['vehicle', 'center']);

        if ($status !== 'all') {
            $query->where('status', $status);
        }

        return $query->get();
    }

    public function findById($id)
    {
        return $this->model->with(['vehicle', 'center'])->find($id);
    }

    public function hasActiveAppointment($vehicleId): bool
    {
        return $this->model->where('vehicle_id', $vehicleId)
            ->whereIn('status', ['pending', 'confirmed'])
            ->exists();
    }

    public function countActiveAppointmentsOnDate($centerId, $date): int
    {
        return $this->model->where('center_id', $centerId)
            ->whereDate('appointment_date', $date)
            ->whereIn('status', ['pending', 'confirmed'])
            ->count();
    }
}
