<?php

namespace App\Http\Resources\Client\User;

use Illuminate\Http\Resources\Json\JsonResource;

class UserResource extends JsonResource
{
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'full_name' => $this->full_name,
            'day_of_birth' => $this->day_of_birth,
            'gender' => $this->gender,
            'email' => $this->email,
            'phone' => $this->phone,
            'is_verified' => $this->citizenCard ? true : false,
            'created_at' => $this->created_at->toDateTimeString(),
        ];
    }
}
