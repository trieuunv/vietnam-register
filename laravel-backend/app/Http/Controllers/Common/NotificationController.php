<?php

namespace App\Http\Controllers\Common;

use App\Helpers\ApiResponse;
use App\Http\Controllers\Controller;
use App\Http\Resources\Common\NotificationResource;
use App\Http\Resources\Common\PaginatedResource;
use App\Services\Common\NotificationService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class NotificationController extends Controller
{
    protected $notificationService;

    public function __construct(NotificationService $notificationService)
    {
        $this->notificationService = $notificationService;
    }

    public function index(Request $request)
    {
        $status = $request->query('status', 'all');
        $limit = $request->query('limit', 10);

        $notifications = $this->notificationService->getByReadStatus(Auth::id(), $status, $limit);

        return ApiResponse::success(new PaginatedResource($notifications, NotificationResource::class));
    }
}
