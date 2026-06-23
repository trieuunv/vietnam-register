<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Client\Vehicle\RegisterVehicleRequest;
use App\Http\Requests\Client\Vehicle\UpdateVehicleRequest;
use App\Http\Resources\Client\Vehicle\GetListVehicleResource;
use App\Http\Resources\Client\Vehicle\GetVehicleDetailResource;
use App\Services\Client\VehicleService;
use Illuminate\Support\Facades\Auth;

class VehicleController extends Controller
{
    protected $vehicleService;

    public function __construct(VehicleService $vehicleService)
    {
        $this->vehicleService = $vehicleService;
    }

    public function register(RegisterVehicleRequest $request)
    {
        $validatedData = $request->validated();

        $vehicle = $this->vehicleService->register(Auth::user(), $validatedData);

        return ApiResponse::success($validatedData);
    }

    public function index()
    {
        $vehicles = $this->vehicleService->getAll(Auth::user());

        return ApiResponse::success(GetListVehicleResource::collection($vehicles));
    }

    public function get($id)
    {
        $vehicle = $this->vehicleService->get(Auth::user(), $id);

        return ApiResponse::success(new GetVehicleDetailResource($vehicle));
    }

    public function update($id, UpdateVehicleRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->vehicleService->update(Auth::user(), $id, $validatedData);

        return ApiResponse::success($updated, 'Updated');
    }
}
