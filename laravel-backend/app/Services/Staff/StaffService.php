<?php

namespace App\Services\Staff;

use App\Repositories\Staff\StaffRepository;

class StaffService
{
    protected $staffRepository;

    public function __construct(StaffRepository $staffRepository)
    {
        $this->staffRepository = $staffRepository;
    }

    public function getByCenterId($centerId)
    {
        return $this->staffRepository->getByCenterId($centerId);
    }
}