<?php

namespace App\Repositories\Client;

use App\Helpers\ApiResponse;
use App\Models\Inspection;
use App\Repositories\Core\BaseRepository;

class InspectionRepository extends BaseRepository
{
    public function __construct(Inspection $model)
    {
        parent::__construct($model);
    }

    public function getByOwner($ownerId)
    {
        return $this->model->whereHas('vehicle.owner', function ($query) use ($ownerId) {
            $query->where('id', $ownerId);
        })->with(['center', 'vehicle'])->get();
    }

    public function findById($id)
    {
        return $this->model->with('criteriaResults', 'criteriaResults.criteria', 'center', 'vehicle')->find($id);
    }
}
