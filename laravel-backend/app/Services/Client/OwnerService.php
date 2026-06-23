<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Client\OwnerRepository;

class OwnerService
{
    protected $ownerRepository;

    public function __construct(OwnerRepository $ownerRepository)
    {
        $this->ownerRepository = $ownerRepository;
    }

    public function updateOwner(User $user, array $data)
    {
        $owner = $this->ownerRepository->findById($user->owner->id);

        if (!$owner) {
            throw new HandlerException('Owner not found.', 404);
        }

        $updated = $this->ownerRepository->update($owner, $data);

        return $updated;
    }
}
