<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Common\Address\UpdateAddressRequest;
use App\Services\Staff\CenterAddressService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class CenterAddressController extends Controller
{
    protected $centerAddressService;

    public function __construct(CenterAddressService $centerAddressService)
    {
        $this->centerAddressService = $centerAddressService;
    }

    public function show()
    {
        $centerAddress = $this->centerAddressService->getCenterAddress(Auth::user());

        return ApiResponse::success($centerAddress);
    }
    
    public function update(UpdateAddressRequest $request)
    {
        $validatedData = $request->validated();

        $centerAddress = $this->centerAddressService->updateCenterAddress(Auth::user(), $validatedData);

        return ApiResponse::success($centerAddress, 'Updated', 201);
    }
}
