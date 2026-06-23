<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Client\OwnerRepository;
use App\Repositories\Client\UserRepository;
use Illuminate\Http\Response;

class UserServices
{
    private $userRepository;
    private $ownerRepository;

    public function __construct(UserRepository $userRepository, OwnerRepository $ownerRepository)
    {
        $this->userRepository = $userRepository;
        $this->ownerRepository = $ownerRepository;
    }

    public function getMe(User $user)
    {
        $info = $this->ownerRepository->findById($user->owner->id);

        if (!$info) {
            throw new HandlerException('Không tìm thấy thông tin', 404);
        }

        return $info;
    }
}
