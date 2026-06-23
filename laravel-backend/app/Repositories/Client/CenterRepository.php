<?php

namespace App\Repositories\Client;

use App\Models\Center;
use App\Repositories\Core\BaseRepository;

class CenterRepository extends BaseRepository
{
    public function __construct(Center $model)
    {
        parent::__construct($model);
    }

    public function findById(int $id)
    {
        return $this->model->with('address')->find($id);
    }

    public function findByAddress($provinceCode = null, $districtCode = null, $wardCode = null)
    {
        return $this->model->whereHas('address', function ($query) use ($provinceCode, $districtCode, $wardCode) {
            if ($provinceCode) {
                $query->where('province_code', $provinceCode);
            }
            if ($districtCode) {
                $query->where('district_code', $districtCode);
            }
            if ($wardCode) {
                $query->where('ward_code', $wardCode);
            }
        })->get();
    }
}
