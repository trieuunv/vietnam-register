<?php

namespace App\Repositories\Staff;

use App\Models\Center;
use App\Models\User;
use App\Repositories\Core\BaseRepository;

class CenterAddressRepository extends BaseRepository
{
    public function __construct(Center $model)
    {
        parent::__construct($model);
    }
}