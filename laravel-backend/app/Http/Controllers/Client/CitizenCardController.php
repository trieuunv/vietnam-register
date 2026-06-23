<?php

namespace App\Http\Controllers\Client;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Client\CitizenCard\AuthenticateCitizenCardRequest;
use App\Services\Client\CitizenCardService;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Auth;

class CitizenCardController extends Controller
{
    protected $citizenCardService;

    public function __construct(CitizenCardService $citizenCardService)
    {
        $this->citizenCardService = $citizenCardService;
    }

    public function authenticate(AuthenticateCitizenCardRequest $request)
    {
        $validatedData = $request->validated();

        $card = $this->citizenCardService->authenticate(Auth::user(), $validatedData);

        return ApiResponse::success($card, 'Authenticated', Response::HTTP_OK);
    }
}
