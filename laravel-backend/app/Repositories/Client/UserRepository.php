<?php

namespace App\Repositories\Client;

use Illuminate\Support\Facades\Auth;

class UserRepository
{
    public function getMe() 
    {
        return Auth::user();
    }
}