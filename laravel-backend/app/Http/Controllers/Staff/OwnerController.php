<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Staff\Owner\GetOwnerDetailResource;
use App\Services\Staff\OwnerService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class OwnerController extends Controller
{
    protected $ownerService;

    public function __construct(OwnerService $ownerService)
    {
        $this->ownerService = $ownerService;
    }

    public function show($id)
    {
        $owner = $this->ownerService->getOwner(Auth::user(), $id);

        return ApiResponse::success(new GetOwnerDetailResource($owner));
    }
}
