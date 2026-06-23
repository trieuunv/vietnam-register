<?php

namespace App\Repositories\Client;

use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AuthRepository
{
    public function register(array $data) 
    {
        $data['password'] = bcrypt($data['password']);

        // Load relation ship ?    
        return User::create($data);
    }
}