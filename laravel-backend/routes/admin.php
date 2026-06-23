<?php

use App\Http\Controllers\Admin\InspectionCriteriaController;
use App\Http\Controllers\Admin\VehicleController;
use Illuminate\Support\Facades\Route;

Route::prefix('vehicle-type')->middleware('modify.response')->group(function () {
    Route::post('/', [VehicleController::class, 'store']);
    Route::delete('/{id}', [VehicleController::class, 'destroy']);
    Route::put('/{id}', [VehicleController::class, 'update']);
});

Route::prefix('inspection-criteria')->middleware('modify.response')->group(function () {
    Route::get('/', [InspectionCriteriaController::class, 'index']);
    Route::post('/', [InspectionCriteriaController::class, 'store']);
    Route::get('{id}', [InspectionCriteriaController::class, 'show']);
    Route::put('{id}', [InspectionCriteriaController::class, 'update']);
    Route::delete('{id}', [InspectionCriteriaController::class, 'destroy']);
});