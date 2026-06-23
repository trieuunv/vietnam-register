<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Staff\Appointment\GetDetailAppointmentResource;
use App\Http\Resources\Staff\Appointment\GetListAppointmentResource;
use App\Services\Staff\AppointmentService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AppointmentController extends Controller
{
    protected $appointmentService;

    public function __construct(AppointmentService $appointmentService)
    {
        $this->appointmentService = $appointmentService;
    }

    public function index(Request $request)
    {
        $filter = [
            'status' => $request->query('status'),
            'date_from' => $request->query('date_from'),
            'date_to' => $request->query('date_to'),
            'owner_name' => $request->query('owner_name'),
            'vehicle_reg_number' => $request->query('vehicle_reg_number'),
        ];

        $appointments = $this->appointmentService->getAll(Auth::user(), $filter);

        return ApiResponse::success(GetListAppointmentResource::collection($appointments));
    }

    public function show($id)
    {
        $appointment = $this->appointmentService->getById(Auth::user(), $id);

        return ApiResponse::success(new GetDetailAppointmentResource($appointment));
    }

    public function confirm($id)
    {
        $updated = $this->appointmentService->confirm(Auth::user(), $id);

        return ApiResponse::success(new GetDetailAppointmentResource($updated), 'Confirmed');
    }

    public function reject($id)
    {
        $updated = $this->appointmentService->reject(Auth::user(), $id);

        return ApiResponse::success($updated, 'Rejected');
    }

    public function start($id)
    {
        $appointment = $this->appointmentService->startInspection(Auth::user(), $id);

        return ApiResponse::success(new GetDetailAppointmentResource($appointment));
    }
}
