<?php

use App\Http\Controllers\Common\NotificationController;
use App\Http\Controllers\Common\VehicleTypeController;
use Illuminate\Support\Facades\Route;

Route::prefix('notification')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [NotificationController::class, 'index']);
});

Route::prefix('vehicle-type')->middleware(['modify.response'])->group(function () {
    Route::get('/', [VehicleTypeController::class, 'index']);
});
