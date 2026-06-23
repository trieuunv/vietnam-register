<?php

use App\Http\Controllers\Client\AppointmentController;
use App\Http\Controllers\Client\AuthController;
use App\Http\Controllers\Client\CenterController;
use App\Http\Controllers\Client\CitizenCardController;
use App\Http\Controllers\Client\InspectionController;
use App\Http\Controllers\Client\OwnerController;
use App\Http\Controllers\Client\UserAddressController;
use App\Http\Controllers\Client\VehicleController;
use Illuminate\Support\Facades\Route;

Route::prefix('auth')->middleware('modify.response')->group(function () {
    Route::post('/register', [AuthController::class, 'register']);
    Route::post('/login', [AuthController::class, 'login']);
    Route::post('/refresh', [AuthController::class, 'refreshAccessToken']);
    Route::middleware(['auth.api'])->group(function () {
        Route::get('/check', [AuthController::class, 'checkAccess']);
    });
});

Route::prefix('owner')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::put('/', [OwnerController::class, 'update']);
});

Route::prefix('citizen-card')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::post('/authenticate', [CitizenCardController::class, 'authenticate']);
});

Route::prefix('address')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [UserAddressController::class, 'get']);
    Route::post('/', [UserAddressController::class, 'create']);
    Route::put('/', [UserAddressController::class, 'update']);
});

Route::prefix('vehicle')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [VehicleController::class, 'index']);
    Route::get('/{id}', [VehicleController::class, 'get']);
    Route::post('/register', [VehicleController::class, 'register']);
    Route::put('/{id}', [VehicleController::class, 'update']);
});

Route::prefix('appointment')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/{id}', [AppointmentController::class, 'findById'])->where('id', '[0-9]+');
    Route::get('/{status}', [AppointmentController::class, 'getByStatus'])->where('status', '[a-zA-Z]+');
    Route::post('/create', [AppointmentController::class, 'create']);
});

Route::prefix('centers')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/by-address', [CenterController::class, 'findByAddress']);
    Route::get('/{id}', [CenterController::class, 'show']);
});

Route::prefix('inspections')->middleware(['modify.response', 'auth.api'])->group(function () {
    Route::get('/', [InspectionController::class, 'index']);
    Route::get('/{id}', [InspectionController::class, 'findById']);
});
