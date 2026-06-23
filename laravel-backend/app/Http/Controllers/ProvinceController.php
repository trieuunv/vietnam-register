<?php

namespace App\Http\Controllers;

use App\Helpers\ApiResponse;
use App\Http\Requests\ProvinceRequest;
use App\Services\ProvinceService;
use Illuminate\Http\Request;

class ProvinceController extends Controller
{
    protected $provinceService;

    public function __construct(ProvinceService $provinceService)
    {
        $this->provinceService = $provinceService;
    }

    public function getProvinces()
    {
        $provinces = $this->provinceService->getProvinces();

        return ApiResponse::success($provinces);
    }

    public function getDistricts(Request $request)
    {
        $districts = $this->provinceService->getDistricts($request['province_id']);

        return ApiResponse::success($districts);
    }

    public function getWards(Request $request)
    {
        $wards = $this->provinceService->getWards($request['district_id']);

        return ApiResponse::success($wards);
    }

    public function getLocation(ProvinceRequest $request)
    {
        $validatedData = $request->validated();
        $location = $this->provinceService->getLocation($validatedData['province_code'], $validatedData['district_code'], $validatedData['ward_code']);

        return ApiResponse::success($location);
    }
}
