<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Staff\Vehicle\GetListVehicleCenterRequest;
use App\Http\Resources\Common\PaginatedResource;
use App\Http\Resources\Staff\Vehicle\GetDetailVehicleResource;
use App\Http\Resources\Staff\Vehicle\GetListVehicleCenterResource;
use App\Services\Staff\VehicleService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class VehicleController extends Controller
{
    protected $vehicleService;

    public function __construct(VehicleService $vehicleService)
    {
        $this->vehicleService = $vehicleService;
    }

    public function index(GetListVehicleCenterRequest $request)
    {
        $validatedData = $request->validated();

        $vehicles = $this->vehicleService->getCenterVehicles(Auth::user(), $validatedData);

        return ApiResponse::success(new PaginatedResource($vehicles, GetListVehicleCenterResource::class));
    }

    public function show($vehicleId)
    {
        $vehicle = $this->vehicleService->getVehicleDetails(
            auth()->user(),
            $vehicleId
        );

        return ApiResponse::success(new GetDetailVehicleResource($vehicle));
    }
}
