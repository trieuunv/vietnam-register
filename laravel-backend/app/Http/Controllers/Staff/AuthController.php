<?php

namespace App\Http\Controllers\Staff;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Staff\Auth\LoginRequest;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Tymon\JWTAuth\Facades\JWTAuth;

class AuthController extends Controller
{
    public function login(LoginRequest $request) 
    {
        $credentials = $request->only(['email', 'password']);

        if (!$token = auth('api')->attempt($credentials)) {
            return ApiResponse::error('Invalid credentials', null, Response::HTTP_UNAUTHORIZED);
        }

        $user = auth('api')->user();

        if ($user->role != 'staff') {
            return ApiResponse::error('Invalid credentials', null, Response::HTTP_UNAUTHORIZED);
        }

        $refreshToken = JWTAuth::fromUser($user);

        $data = [
            'access_token' => $token,
            'refresh_token' => $refreshToken,
            'token_type' => 'bearer',
            'expires_in' => config('jwt.ttl') * 60,
        ];

        return ApiResponse::success($data, 'Login successful', Response::HTTP_OK);
    }
}
