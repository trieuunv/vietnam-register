<?php

namespace App\Repositories\Client;

use App\Models\Vehicle;
use App\Repositories\Core\BaseRepository;

class VehicleRepository extends BaseRepository
{
    public function __construct(Vehicle $model)
    {
        parent::__construct($model);
    }

    public function register(array $data): Vehicle
    {
        return $this->create($data);
    }

    public function getByOwner($ownerId)
    {
        return $this->model->where('owner_id', $ownerId)
            ->with('vehicleType')
            ->get();
    }

    public function findWithOwner($ownerId, $vehicleId)
    {
        return $this->model->where('id', $vehicleId)
            ->where('owner_id', $ownerId,)
            ->first();
    }
}
