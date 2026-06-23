<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Staff\CenterAddressRepository;

class CenterAddressService
{
    protected $centerAddressRepository;

    public function __construct(CenterAddressRepository $centerAddressRepository)
    {
        $this->centerAddressRepository = $centerAddressRepository;
    }

    public function updateCenterAddress(User $staff, array $data)
    {
        $data['updated_by'] = $staff->id;
        $centerAddress = $this->centerAddressRepository->findById($staff->center_id);

        if (!$centerAddress) {
            return $this->centerAddressRepository->create($data);
        }
    
        return $this->centerAddressRepository->update($centerAddress, $data);
    }

    public function getCenterAddress(User $staff)
    {
        $centerAddress = $this->centerAddressRepository->findById($staff->center_id);

        if (!$centerAddress) {
            throw new HandlerException('Center address not found', 404);
        }

        return $centerAddress;
    }
}