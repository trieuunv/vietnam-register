<?php

namespace App\Services\Client;

use App\Exceptions\HandlerException;
use App\Helpers\ApiResponse;
use App\Models\User;
use App\Repositories\Client\RegistrationRequestRepository;
use App\Repositories\Client\VehicleRepository;
use Illuminate\Support\Facades\DB;

class VehicleService
{
    protected $vehicleRepository;
    protected $registrationRequestRepository;

    public function __construct(
        VehicleRepository $vehicleRepository,
        RegistrationRequestRepository $registrationRequestRepository
    ) {
        $this->vehicleRepository = $vehicleRepository;
        $this->registrationRequestRepository = $registrationRequestRepository;
    }

    public function register(User $user, array $data)
    {
        if (!$user->owner) {
            throw new HandlerException('Only owners can register vehicles', 403);
        }

        $data['owner_id'] = $user->owner->id;

        return DB::transaction(function () use ($user, $data) {
            $vehicle = $this->vehicleRepository->register($data);

            $registrationRequestData = [
                'owner_id'     => $user->owner->id,
                'vehicle_id'   => $vehicle->id,
                'request_type' => 'new',
                'status'       => 'pending',
                'submitted_at' => now(),
            ];

            $this->registrationRequestRepository->create($registrationRequestData);

            return $vehicle;
        });
    }

    public function getAll(User $user)
    {
        $owner = $user->owner;

        if (!$owner) {
            throw new HandlerException('Owner not found', 404);
        }

        return $this->vehicleRepository->getByOwner($owner->id);
    }

    public function get(User $user, $vehicleId)
    {
        $owner = $user->owner;

        if (!$owner) {
            throw new HandlerException('Owner not found', 404);
        }

        return $this->vehicleRepository->findWithOwner($owner->id, $vehicleId);
    }

    public function update(User $user, $vehicleId, array $data)
    {
        $owner = $user->owner;

        if (!$owner) {
            throw new HandlerException('Owner not found', 404);
        }

        $vehicle = $this->vehicleRepository->findWithOwner($owner->id, $vehicleId);

        if (!$vehicle) {
            throw new HandlerException('Vehicle not found', 404);
        }

        $updated = $this->vehicleRepository->update($vehicle, $data);

        return $updated;
    }
}
