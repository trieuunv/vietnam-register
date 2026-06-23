<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Client\InspectionRepository;

class InspectionService
{
    protected $inspectionRepository;

    public function __construct(InspectionRepository $inspectionRepository)
    {
        $this->inspectionRepository = $inspectionRepository;
    }

    public function getAll(User $user)
    {
        return $this->inspectionRepository->getByOwner($user->owner->id);
    }

    public function getById(User $user, $inspectionId)
    {
        $inspection = $this->inspectionRepository->findById($inspectionId);

        if (!$inspection || $inspection->vehicle->owner_id !== $user->owner->id) {
            throw new HandlerException('Inspection not found', 404);
        }

        return $inspection;
    }
}
