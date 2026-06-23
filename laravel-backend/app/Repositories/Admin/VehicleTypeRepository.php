<?php

namespace App\Repositories\Admin;

use App\Models\VehicleType;
use App\Repositories\Core\BaseRepository;

class VehicleTypeRepository extends BaseRepository
{
    public function __construct(VehicleType $model)
    {
        parent::__construct($model);
    }
}