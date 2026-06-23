<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Models\Vehicle;
use App\Repositories\Staff\VehicleRepository;
use Carbon\Carbon;

class VehicleService
{
    protected $vehicleRepository;

    public function __construct(VehicleRepository $vehicleRepository)
    {
        $this->vehicleRepository = $vehicleRepository;
    }

    public function getCenterVehicles(User $staff, array $filters = [])
    {
        return $this->vehicleRepository->getCenterVehicles(
            $staff->center_id,
            $filters
        );
    }

    public function getVehicleDetails(User $staff, int $vehicleId)
    {
        $vehicle = $this->vehicleRepository->findVehicle(
            $vehicleId
        );

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        return $vehicle;
    }
}
