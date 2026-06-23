<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Staff\CriteriaResult\UpdateCriteriaResultRequest;
use App\Services\Staff\CriteriaResultService;
use Illuminate\Support\Facades\Auth;

class CriteriaResultController extends Controller
{
    protected $criteriaResultService;

    public function __construct(CriteriaResultService $criteriaResultService)
    {
        $this->criteriaResultService = $criteriaResultService;
    }

    public function update($criteriaResultId, UpdateCriteriaResultRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->criteriaResultService->update(Auth::user(), $criteriaResultId, $validatedData);

        return ApiResponse::success($updated, 'Updated');
    }
}
