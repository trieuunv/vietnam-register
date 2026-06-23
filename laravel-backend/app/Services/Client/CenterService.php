<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Repositories\Client\CenterRepository;

class CenterService
{
    protected $centerRepository;

    public function __construct(CenterRepository $centerRepository)
    {
        $this->centerRepository = $centerRepository;
    }

    public function getCenter($id)
    {
        $center = $this->centerRepository->findById($id);

        if (!$center) {
            throw new HandlerException('Center not found', 404);
        }

        return $center;
    }

    public function getCentersByAddress($provinceCode = null, $districtCode = null, $wardCode = null)
    {
        return $this->centerRepository->findByAddress(
            $provinceCode,
            $districtCode,
            $wardCode
        );
    }
}
