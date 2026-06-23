<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Client\Appointment\CreateAppointmentRequest;
use App\Services\Client\AppointmentService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AppointmentController extends Controller
{
    protected $appointmentService;

    public function __construct(AppointmentService $appointmentService)
    {
        $this->appointmentService = $appointmentService;
    }

    public function create(CreateAppointmentRequest $request)
    {
        $validatedData = $request->validated();

        $appointment = $this->appointmentService->create(Auth::user(), $validatedData);

        return ApiResponse::success($appointment, 'Created', 201);
    }

    public function getByStatus($status)
    {
        $appointments = $this->appointmentService->getByStatus(Auth::user(), $status);

        return ApiResponse::success($appointments);
    }

    public function findById($id)
    {
        $appointment = $this->appointmentService->getById(Auth::user(), $id);

        return ApiResponse::success($appointment);
    }
}
