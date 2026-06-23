<?php

namespace App\Services\Common;

use App\Repositories\Common\VehicleTypeRepository;

class VehicleTypeService
{
    protected $vehicleTypeRepository;

    public function __construct(VehicleTypeRepository $vehicleTypeRepository)
    {
        $this->vehicleTypeRepository = $vehicleTypeRepository;
    }

    public function getAll()
    {
        return $this->vehicleTypeRepository->getAll();
    }
}
