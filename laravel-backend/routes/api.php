<?php

use App\Http\Controllers\Client\AuthController;
use App\Http\Controllers\Client\ClientCommentController;
use App\Http\Controllers\Client\UserController;
use App\Http\Controllers\ProvinceController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('/comment', [ClientCommentController::class, 'create']);

Route::prefix('auth')->middleware('modify.response')->group(function () {
    Route::post('/register', [AuthController::class, 'register']);
    Route::post('/login', [AuthController::class, 'login']);
    Route::post('/refresh', [AuthController::class, 'refreshAccessToken']);
    Route::middleware(['auth.api'])->group(function () {
        Route::get('/check', [AuthController::class, 'checkAccess']);
    });
});

Route::prefix('user')
    ->middleware(['auth.api', 'modify.response'])
    ->group(function () {
        Route::get('/', [UserController::class, 'getMe']);
    }
);

Route::prefix('provinces')->group(function () {
    Route::get('/', [ProvinceController::class, 'getProvinces']);
    Route::get('/get-districts/{province_id}', [ProvinceController::class, 'getDistricts']);
    Route::get('/get-wards/{district_id}', [ProvinceController::class, 'getWards']);
    Route::post('/get-location', [ProvinceController::class, 'getLocation']);
});