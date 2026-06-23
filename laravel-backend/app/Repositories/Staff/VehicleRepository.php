<?php

namespace App\Repositories\Staff;

use App\Models\Vehicle;
use App\Repositories\Core\BaseRepository;
use Illuminate\Contracts\Pagination\Paginator;

class VehicleRepository extends BaseRepository
{
    public function __construct(Vehicle $model)
    {
        parent::__construct($model);
    }

    public function getCenterVehicles(int $centerId, array $filters = []): Paginator
    {
        $query = $this->model->whereHas('inspections', function ($q) use ($centerId) {
            $q->where('center_id', $centerId);
        })
            ->with(['owner', 'vehicleType', 'latestInspection']);

        if (isset($filters['registration_status'])) {
            $query->where('registration_status', $filters['registration_status']);
        }

        if (isset($filters['status'])) {
            $query->where('status', $filters['status']);
        }

        return $query->paginate($filters['per_page'] ?? 15);
    }

    public function findVehicle(int $vehicleId): ?Vehicle
    {
        return $this->model->whereHas('inspections', function ($q) {
            $q->whereIn('result', ['passed', 'failed', 'conditional']);
        })
            ->with([
                'owner.user',
                'vehicleType',
                'inspections' => function ($q) {
                    $q->whereIn('result', ['passed', 'failed', 'conditional']);
                },
                'inspections.center'
            ])
            ->find($vehicleId);
    }

    public function updateRegistrationStatus(int $vehicleId, string $status): Vehicle
    {
        $vehicle = $this->model->findOrFail($vehicleId);

        $vehicle->update([
            'registration_status' => $status,
            'status' => $status === 'registered' ? 'active' : 'inactive'
        ]);

        return $vehicle;
    }

    public function searchVehicles(int $centerId, string $keyword, int $perPage = 15): Paginator
    {
        return $this->model->whereHas('inspections', function ($q) use ($centerId) {
            $q->where('center_id', $centerId);
        })
            ->where(function ($q) use ($keyword) {
                $q->where('registration_number', 'like', "%{$keyword}%")
                    ->orWhere('chassis_number', 'like', "%{$keyword}%")
                    ->orWhere('engine_number', 'like', "%{$keyword}%")
                    ->orWhere('brand', 'like', "%{$keyword}%")
                    ->orWhere('model', 'like', "%{$keyword}%");
            })
            ->paginate($perPage);
    }

    public function getVehiclesNeedInspection(int $centerId): Paginator
    {
        return $this->model->whereHas('inspections', function ($q) use ($centerId) {
            $q->where('center_id', $centerId)
                ->where('next_inspection_date', '<=', now()->addDays(30));
        })
            ->where('registration_status', 'registered')
            ->where('status', 'active')
            ->paginate(15);
    }

    public function getVehiclesWithExpiredRegistration(int $centerId): Paginator
    {
        return $this->model->whereHas('inspections', function ($q) use ($centerId) {
            $q->where('center_id', $centerId);
        })
            ->where('registration_status', 'registered')
            ->where('status', 'active')
            ->whereDoesntHave('inspections', function ($q) {
                $q->where('next_inspection_date', '>', now());
            })
            ->paginate(15);
    }
}
