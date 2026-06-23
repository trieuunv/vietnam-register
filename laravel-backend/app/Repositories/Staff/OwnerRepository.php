<?php

namespace App\Repositories\Staff;

use App\Models\Owner;
use App\Repositories\Core\BaseRepository;

class OwnerRepository extends BaseRepository
{
    public function __construct(Owner $model)
    {
        parent::__construct($model);
    }

    public function findOwner($id)
    {
        return $this->model->where('id', $id)
            ->with('vehicles', 'vehicles.latestPassedInspection', 'citizenCard')
            ->first();
    }
}
