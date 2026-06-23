<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Staff\OwnerRepository;

class OwnerService
{
    protected $ownerRepository;

    public function __construct(OwnerRepository $ownerRepository)
    {
        $this->ownerRepository = $ownerRepository;
    }

    public function getOwner(User $staff, $ownerId)
    {
        $owner = $this->ownerRepository->findOwner($ownerId);

        if (!$owner) {
            throw new HandlerException('Owner not found', 404);
        }

        return $owner;
    }
}
