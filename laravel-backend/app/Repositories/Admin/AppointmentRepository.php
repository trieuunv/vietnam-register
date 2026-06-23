<?php

namespace App\Repositories\Admin;

use App\Models\Appointment;
use App\Repositories\Core\BaseRepository;

class AppointmentRepository extends BaseRepository
{
    public function __construct(Appointment $model)
    {
        parent::__construct($model);
    }

    // public function 
}