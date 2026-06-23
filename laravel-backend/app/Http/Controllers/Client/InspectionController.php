<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Services\Client\InspectionService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class InspectionController extends Controller
{
    protected $inspectionService;

    public function __construct(InspectionService $inspectionService)
    {
        $this->inspectionService = $inspectionService;
    }

    public function index()
    {
        $inspections = $this->inspectionService->getAll(Auth::user());

        return ApiResponse::success($inspections);
    }

    public function findById($id)
    {
        $inspections = $this->inspectionService->getById(Auth::user(), $id);

        return ApiResponse::success($inspections);
    }
}
