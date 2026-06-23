<?php

namespace App\Repositories\Client;

use App\Models\RegistrationRequest;
use App\Repositories\Core\BaseRepository;

class RegistrationRequestRepository extends BaseRepository
{
    public function __construct(RegistrationRequest $model)
    {
        parent::__construct($model);
    }

    public function create(array $data): RegistrationRequest
    {
        return $this->model->create($data);
    }
}