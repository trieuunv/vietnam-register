<?php 

namespace App\Services\Client;

use App\Models\User;
use App\Repositories\Client\AuthRepository;
use App\Repositories\Client\OwnerRepository;
use Illuminate\Support\Facades\DB;
use Tymon\JWTAuth\Facades\JWTAuth;

class AuthService
{
    private $authRepository;
    protected $ownerRepository;

    public function __construct(
        AuthRepository $authRepository,
        OwnerRepository $ownerRepository
    ) {
        $this->authRepository = $authRepository;
        $this->ownerRepository = $ownerRepository;
    }

    public function register(array $data) 
    {
        $data['password'] = bcrypt($data['password']);
        $data['role'] = 'customer';
        $user = User::create($data);

        $ownerData = [
            'user_id' => $user->id,
            'full_name' => $data['full_name'] ?? null,
            'email' => $data['email'] ?? null,
            'phone' => $data['phone'] ?? null,
        ];

        $this->ownerRepository->create($ownerData);

        return $user;
    }
}