<?php

namespace App\Http\Controllers\Admin;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\Vehicle\StoreVehicleTypeRequest;
use App\Http\Requests\Admin\Vehicle\UpdateVehicleTypeRequest;
use App\Services\Admin\VehicleService;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class VehicleController extends Controller
{
    protected $vehicleService;

    public function __construct(VehicleService $vehicleService)
    {
        $this->vehicleService = $vehicleService;
    }

    public function store(StoreVehicleTypeRequest $request)
    {
        $validatedData = $request->validated();

        $vehicleType = $this->vehicleService->create($validatedData);

        return ApiResponse::success($vehicleType, 'Success', Response::HTTP_CREATED);
    }

    public function destroy($id)
    {
        $result = $this->vehicleService->delete($id);

        if (!$result) {
            ApiResponse::serverError('Failed to delete vehicle.');
        }

        return ApiResponse::success($result, 'Deleted', Response::HTTP_NO_CONTENT);
    }

    public function update($id, UpdateVehicleTypeRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->vehicleService->update($id, $validatedData);

        return ApiResponse::success($updated, 'Vehicle type updated successfully.');
    }
}
