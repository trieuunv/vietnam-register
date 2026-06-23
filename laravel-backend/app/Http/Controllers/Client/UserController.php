<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Client\User\UserResource;
use App\Services\Client\UserServices;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Auth;

class UserController extends Controller
{
    private $userService;

    public function __construct(UserServices $userService)
    {
        $this->userService = $userService;
    }

    public function getMe()
    {
        $user = $this->userService->getMe(Auth::user());

        return ApiResponse::success(new UserResource($user), 'Fetch user successful', Response::HTTP_OK);
    }
}
