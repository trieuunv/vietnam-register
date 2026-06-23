<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Client\Owner\UpdateOwnerRequest;
use App\Repositories\Client\OwnerRepository;
use App\Services\Client\OwnerService;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Auth;

class OwnerController extends Controller
{
    protected $ownerService;

    public function __construct(OwnerService $ownerService)
    {
        $this->ownerService = $ownerService;
    }

    public function update(UpdateOwnerRequest $request)
    {
        $validatedData = $request->validated();

        $updated = $this->ownerService->updateOwner(Auth::user(), $validatedData);

        return ApiResponse::success($updated, 'Updated', Response::HTTP_OK);
    }
}
