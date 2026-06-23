<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Client\Center\GetListCenterResource;
use App\Services\Client\CenterService;
use Illuminate\Http\Request;

class CenterController extends Controller
{
    protected $centerService;

    public function __construct(CenterService $centerService)
    {
        $this->centerService = $centerService;
    }

    public function show($id)
    {
        $center = $this->centerService->getCenter($id);

        return ApiResponse::success($center);
    }

    public function findByAddress(Request $request)
    {
        $provinceCode = $request->input('province_code');
        $districtCode = $request->input('district_code');
        $wardCode = $request->input('ward_code');

        $centers = $this->centerService->getCentersByAddress(
            $provinceCode,
            $districtCode,
            $wardCode
        );

        return ApiResponse::success(GetListCenterResource::collection($centers));
    }
}
