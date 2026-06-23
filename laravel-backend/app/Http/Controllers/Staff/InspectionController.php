<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Staff\Inspection\CompleteInspectionRequest;
use App\Http\Requests\Staff\Inspection\SubmitInspectionRequest;
use App\Http\Requests\Staff\Inspection\UpdateInspectionRequest;
use App\Http\Requests\Staff\Inspection\UpdateResultRequest;
use App\Http\Requests\Staff\InspectionResult\UpdateCriteriaResultRequest;
use App\Http\Resources\Staff\Inspection\GetDetailInspectionResource;
use App\Http\Resources\Staff\Inspection\GetListInspectionResource;
use App\Services\Staff\CriteriaResultService;
use App\Services\Staff\InspectionService;
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
        $inspections = $this->inspectionService->getByCenterId(Auth::user());

        return ApiResponse::success(GetListInspectionResource::collection($inspections));
    }

    public function show($id)
    {
        $inspection = $this->inspectionService->getDetailById(Auth::user(), $id);

        return ApiResponse::success(new GetDetailInspectionResource($inspection));
    }

    public function complete($id)
    {
        $inspection = $this->inspectionService->completeInspection(Auth::user(), $id);

        return ApiResponse::success(new GetDetailInspectionResource($inspection));
    }

    public function cancel($id)
    {
        $inspection = $this->inspectionService->cancelInspection(Auth::user(), $id);

        return ApiResponse::success(new GetDetailInspectionResource($inspection));
    }

    public function submit($id)
    {
        $inspection = $this->inspectionService->submitInspection(Auth::user(), $id);

        return ApiResponse::success(new GetDetailInspectionResource($inspection));
    }

    public function updateResult($id, UpdateResultRequest $request)
    {
        $validatedData = $request->validated();

        $rs = $this->inspectionService->updateResult(Auth::user(), $id, $validatedData);

        return ApiResponse::success(null, 'Updated');
    }

    public function update($id, UpdateInspectionRequest $request)
    {
        $validatedData = $request->validated();

        $inspection = $this->inspectionService->update(Auth::user(), $id, $validatedData);

        return ApiResponse::success(new GetDetailInspectionResource($inspection));
    }
}
