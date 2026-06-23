<?php

namespace App\Repositories\Admin;

use App\Models\InspectionCriteria;
use App\Repositories\Core\BaseRepository;

class InspectionCriteriaRepository extends BaseRepository
{
    public function __construct(InspectionCriteria $model)
    {
        parent::__construct($model);
    }
}