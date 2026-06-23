<?php

namespace App\Repositories\Client;

use App\Models\Owner;
use App\Models\User;
use App\Repositories\Core\BaseRepository;

class OwnerRepository extends BaseRepository
{
    public function __construct(Owner $model)
    {
        parent::__construct($model);
    }

    public function findById($ownerId)
    {
        return $this->model->with('citizenCard')->find($ownerId);
    }
}
