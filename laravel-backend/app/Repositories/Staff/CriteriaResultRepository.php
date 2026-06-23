<?php

namespace App\Repositories\Staff;

use App\Models\CriteriaResult;
use App\Models\InspectionResult;
use App\Repositories\Core\BaseRepository;

class CriteriaResultRepository extends BaseRepository
{
    public function __construct(CriteriaResult $model)
    {
        parent::__construct($model);
    }

    public function updateOrCreate(array $conditions, array $values)
    {
        return $this->model->updateOrCreate($conditions, $values);
    }
}
