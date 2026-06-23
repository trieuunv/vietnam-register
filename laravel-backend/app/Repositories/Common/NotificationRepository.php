<?php

namespace App\Repositories\Common;

use App\Models\Notification;
use App\Repositories\Core\BaseRepository;

class NotificationRepository extends BaseRepository
{
    public function __construct(Notification $model)
    {
        parent::__construct($model);
    }

    public function getByReadStatus($userId, $status = 'all', $limit = 20)
    {
        $query = $this->model->where('user_id', $userId);

        if ($status === 'read') {
            $query->where('is_read', true);
        } elseif ($status === 'unread') {
            $query->where('is_read', false);
        }

        return $query->orderByDesc('created_at')->paginate($limit);
    }
}