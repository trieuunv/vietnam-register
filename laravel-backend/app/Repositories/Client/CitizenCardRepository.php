<?php

namespace App\Repositories\Client;

use App\Models\CitizenCard;
use App\Repositories\Core\BaseRepository;

class CitizenCardRepository extends BaseRepository
{
    public function __construct(CitizenCard $model)
    {
        parent::__construct($model);
    }

    public function findByOwnerId($ownerId)
    {
        return $this->model->where('owner_id', $ownerId)->first();
    }
}