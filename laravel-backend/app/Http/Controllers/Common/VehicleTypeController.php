<?php

namespace App\Http\Controllers\Common;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Common\GetListVehicleTypeResource;
use App\Services\Common\VehicleTypeService;
use Illuminate\Http\Request;

class VehicleTypeController extends Controller
{
    protected $vehicleTypeService;

    public function __construct(VehicleTypeService $vehicleTypeService)
    {
        $this->vehicleTypeService = $vehicleTypeService;
    }

    public function index()
    {
        $vehicleTypes = $this->vehicleTypeService->getAll();

        return ApiResponse::success(GetListVehicleTypeResource::collection($vehicleTypes));
    }
}
