<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Client\CitizenCardRepository;
use App\Repositories\Client\OwnerRepository;

class CitizenCardService
{
    protected $citizenCardRepository;
    protected $ownerRepository;

    public function __construct(
        CitizenCardRepository $citizenCardRepository,
        OwnerRepository $ownerRepository
    ) {
        $this->citizenCardRepository = $citizenCardRepository;
        $this->ownerRepository = $ownerRepository;
    }

    public function authenticate(User $user, array $data)
    {
        $owner = $this->ownerRepository->findById($user->owner->id);

        if (!$owner) {
            throw new HandlerException('Owner not found.', 404);
        }

        $card = $this->citizenCardRepository->findByOwnerId($owner->id);

        if ($card) {
            $card = $this->citizenCardRepository->update($card, $data);
        } else {
            $data['owner_id'] = $owner->id;
            $card = $this->citizenCardRepository->create($data);
        }

        return $card;
    }
}
