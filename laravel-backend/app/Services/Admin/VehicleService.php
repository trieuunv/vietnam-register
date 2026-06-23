<?php

namespace App\Services\Admin;

use App\Exceptions\HandlerException;
use App\Helpers\ApiResponse;
use App\Repositories\Admin\VehicleTypeRepository;

class VehicleService
{
    protected $vehicleTypeRepository;

    public function __construct(VehicleTypeRepository $vehicleTypeRepository)
    {
        $this->vehicleTypeRepository = $vehicleTypeRepository;
    }

    public function create(array $data)
    {
        return $this->vehicleTypeRepository->create($data);
    }

    public function delete($id)
    {
        $vehicle = $this->vehicleTypeRepository->findById($id);

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        return $this->vehicleTypeRepository->delete($vehicle);
    }

    public function update($id, array $data)
    {
        $vehicle = $this->vehicleTypeRepository->findById($id);

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        $vehicleType = $this->vehicleTypeRepository->update($vehicle, $data);

        return $vehicleType;
    }
}