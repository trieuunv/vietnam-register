<?php

use App\Http\Controllers\Staff\AppointmentController;
use App\Http\Controllers\Staff\AuthController;
use App\Http\Controllers\Staff\CriteriaResultController;
use App\Http\Controllers\Staff\InspectionController;
use App\Http\Controllers\Staff\InspectionResultController;
use App\Http\Controllers\Staff\OwnerController;
use App\Http\Controllers\Staff\VehicleController;
use Illuminate\Support\Facades\Route;

Route::prefix('auth')->middleware(['modify.response'])->group(function () {
    Route::post('/login', [AuthController::class, 'login']);
});

Route::prefix('appointment')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [AppointmentController::class, 'index']);
    Route::get('/{id}', [AppointmentController::class, 'show']);
    Route::put('/{id}/confirm', [AppointmentController::class, 'confirm']);
    Route::put('/{id}/reject', [AppointmentController::class, 'reject']);
    Route::post('/{id}/start', [AppointmentController::class, 'start']);
});

Route::prefix('inspection')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [InspectionController::class, 'index']);
    Route::get('/{id}', [InspectionController::class, 'show']);
    Route::post('/complete/{id}', [InspectionController::class, 'complete']);
    Route::post('/cancel/{id}', [InspectionController::class, 'cancel']);
    Route::post('/submit/{id}', [InspectionController::class, 'submit']);
    Route::put('/update-result/{id}', [InspectionController::class, 'updateResult']);
    Route::put('/{id}', [InspectionController::class, 'update']);
});

Route::prefix('inspection-result')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::post('/', [CriteriaResultController::class, 'store']);
    Route::put('/{id}', [CriteriaResultController::class, 'update']);
});

Route::prefix('vehicle')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [VehicleController::class, 'index']);
    Route::get('/{id}', [VehicleController::class, 'show']);
});

Route::prefix('owner')->middleware(['modify.response', 'auth.api'])->group(function () {
    // Route::get('/', [VehicleController::class, 'index']);
    Route::get('/{id}', [OwnerController::class, 'show']);
});
