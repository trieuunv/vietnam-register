<?php

namespace App\Repositories\Client;

use App\Models\User;
use App\Models\UserAddress;
use App\Repositories\Core\BaseRepository;

class UserAddressRepository extends BaseRepository
{
    public function __construct(UserAddress $model)
    {
        parent::__construct($model);
    }

    public function getByUserId($userId)
    {
        $user = User::findOrFail($userId);

        return $user->address;
    }
}