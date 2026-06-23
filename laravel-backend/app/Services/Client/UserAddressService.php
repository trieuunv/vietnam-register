<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Repositories\Client\UserAddressRepository;

class UserAddressService
{
    protected $userAddressRepository;

    public function __construct(UserAddressRepository $userAddressRepository)
    {
        $this->userAddressRepository = $userAddressRepository;
    }

    public function get($userId)
    {
        $address = $this->userAddressRepository->getByUserId($userId);

        if (!$address) {
            throw new HandlerException('Address not found.', 404);
        }

        return $address;
    }

    public function create($userId, array $data)
    {
        $data['user_id'] = $userId;
        return $this->userAddressRepository->create($data);
    }

    public function update($userId, array $data)
    {
        $address = $this->userAddressRepository->getByUserId($userId);

        if (!$address) {
            throw new HandlerException('Address not found.', 404);
        }

        $updated = $this->userAddressRepository->update($address, $data);

        return $updated;
    }
}