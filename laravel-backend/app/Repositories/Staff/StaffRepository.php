<?php

namespace App\Repositories\Staff;

use App\Models\User;
use App\Repositories\Core\BaseRepository;

class StaffRepository extends BaseRepository
{
    public function __construct(User $model)
    {
        parent::__construct($model);
    }

    public function getByCenterId($center_id)
    {
        return $this->model->where('center_id', $center_id)->get();
    }
}