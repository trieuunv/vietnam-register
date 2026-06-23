<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Client\Address\CreateUserAddressRequest;
use App\Http\Requests\Client\Address\UpdateUserAddressRequest;
use App\Services\Client\UserAddressService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class UserAddressController extends Controller
{
    protected $userAddressService;

    public function __construct(UserAddressService $userAddressService)
    {
        $this->userAddressService = $userAddressService;
    }

    public function get()
    {
        $address = $this->userAddressService->get(Auth::id());

        return ApiResponse::success($address);
    }

    public function create(CreateUserAddressRequest $request)
    {
        $validatedData = $request->validated();

        $address = $this->userAddressService->create(Auth::id(), $validatedData);

        return ApiResponse::success($address, 'Created', 201);
    }

    public function update(UpdateUserAddressRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->userAddressService->update(Auth::id(), $validatedData);

        return ApiResponse::success($validatedData);
    }
}
