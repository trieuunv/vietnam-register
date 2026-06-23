<?php

namespace App\Http\Controllers\Admin;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\Inspection\StoreInspectionCriteriaRequest;
use App\Http\Requests\Admin\Inspection\UpdateInspectionCriteriaRequest;
use App\Services\Admin\InspectionCriteriaService;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class InspectionCriteriaController extends Controller
{
    protected $inspectionCriteriaService;

    public function __construct(InspectionCriteriaService $inspectionCriteriaService)
    {
        $this->inspectionCriteriaService = $inspectionCriteriaService;
    }

    public function index()
    {
        $list = $this->inspectionCriteriaService->getAll();

        return ApiResponse::success($list);
    }

    public function store(StoreInspectionCriteriaRequest $request)
    {
        $validatedData = $request->validated();

        $newData = $this->inspectionCriteriaService->create($validatedData);

        return ApiResponse::success($newData, 'Created', Response::HTTP_CREATED);
    }

    public function show($id)
    {
        $item = $this->inspectionCriteriaService->getById($id);

        return ApiResponse::success($item);
    }

    public function update($id, UpdateInspectionCriteriaRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->inspectionCriteriaService->update($id, $validatedData);
        
        return ApiResponse::success($updated, 'Updated');
    }

    public function destroy($id)
    {
        $result = $this->inspectionCriteriaService->delete($id);

        if (!$result) {
            return ApiResponse::serverError('Failed to delete inspection criteria.');
        }

        return ApiResponse::success(null, 'Deleted', Response::HTTP_NO_CONTENT);
    }
}
