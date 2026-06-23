<?php

namespace App\Repositories\Staff;

use App\Models\InspectionCriteria;
use App\Repositories\Core\BaseRepository;

class InspectionCriteriaRepository extends BaseRepository
{
    public function __construct(InspectionCriteria $model)
    {
        parent::__construct($model);
    }

    public function getByVehicleType($vehicleTypeId)
    {
        return $this->model->where('vehicle_type_id', $vehicleTypeId)->get();
    }
}
