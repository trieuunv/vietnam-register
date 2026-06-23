<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Requests\Client\Auth\LoginRequest;
use App\Http\Requests\Client\Auth\RefreshAccessToken;
use App\Http\Requests\Client\Auth\RefreshAccessTokenRequest;
use App\Http\Requests\Client\Auth\RegisterRequest;
use App\Models\User;
use App\Services\Client\AuthService;
use Illuminate\Http\Response;
use Tymon\JWTAuth\Facades\JWTAuth;

class AuthController 
{
    private $authService;

    public function __construct(AuthService $authService) { 
        $this->authService = $authService;
    }

    public function login(LoginRequest $request) 
    {
        $credentials = $request->only(['email', 'password']);

        if (!$token = auth('api')->attempt($credentials)) {
            return ApiResponse::error('Invalid credentials', null, Response::HTTP_UNAUTHORIZED);
        }

        $user = auth('api')->user();
        $refreshToken = JWTAuth::fromUser($user);

        $data = [
            'access_token' => $token,
            'refresh_token' => $refreshToken,
            'token_type' => 'bearer',
            'expires_in' => config('jwt.ttl') * 60,
        ];

        return ApiResponse::success($data, 'Login successful', Response::HTTP_OK);
    }

    public function register(RegisterRequest $request)
    {
        $validatedData = $request->validated();

        $user = $this->authService->register($validatedData);
        $accessToken = auth('api')->login($user);
        $refreshToken = JWTAuth::fromUser($user);

        $data = [
            'access_token' => $accessToken,
            'refresh_token' => $refreshToken,
            'token_type' => 'bearer',
            'expires_in' => config('jwt.ttl') * 60,
        ];
        
        return ApiResponse::success($data, 'Registration successful', Response::HTTP_CREATED);
    }

    public function refreshAccessToken(RefreshAccessTokenRequest $request) {
        $requestData = $request->validated();

        try {
            $refreshToken = $requestData['refresh_token'];
            $newToken = JWTAuth::setToken($refreshToken)->refresh();

            $data = [
                'access_token' => $newToken,
                'expires_in' => config('jwt.ttl') * 60
            ];

            return ApiResponse::success($data, 'Refresh access token successful', Response::HTTP_OK);
        } catch (\Tymon\JWTAuth\Exceptions\TokenInvalidException $e) {
            return ApiResponse::error('Token refresh failed', null, Response::HTTP_UNAUTHORIZED);
        }
    }
}   