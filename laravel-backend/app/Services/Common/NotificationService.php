<?php

namespace App\Services\Common;

use App\Repositories\Common\NotificationRepository;

class NotificationService
{
    protected $notificationRepository;

    public function __construct(NotificationRepository $notificationRepository)
    {
        $this->notificationRepository = $notificationRepository;
    }

    public function getByReadStatus($userId, $status, $limit)
    {
        return $this->notificationRepository->getByReadStatus($userId, $status, $limit);
    }
}