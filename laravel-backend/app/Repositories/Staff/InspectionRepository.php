<?php

namespace App\Repositories\Staff;

use App\Models\Inspection;
use App\Repositories\Core\BaseRepository;
use Illuminate\Support\Facades\DB;

class InspectionRepository extends BaseRepository
{
    public function __construct(Inspection $model)
    {
        parent::__construct($model);
    }

    public function getByCenterId($centerId)
    {
        return $this->model->where('center_id', $centerId)
            ->with(['vehicle', 'inspector'])
            ->get();
    }

    public function getDetailById($centerId, $id)
    {
        return $this->model
            ->with('vehicle', 'vehicle.owner', 'vehicle.vehicleType', 'vehicle.owner.citizenCard', 'inspector', 'criteriaResults', 'criteriaResults.criteria')
            ->where('center_id', $centerId)
            ->where('id', $id)
            ->first();
    }
}
