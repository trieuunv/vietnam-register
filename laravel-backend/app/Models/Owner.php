<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Owner extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'user_id',
        'full_name',
        'day_of_birth',
        'gender',
        'email',
        'phone'
    ];

    protected $dates = ['day_of_birth'];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function vehicles()
    {
        return $this->hasMany(Vehicle::class);
    }

    public function citizenCard()
    {
        return $this->hasOne(CitizenCard::class);
    }

    public function registrationRequests()
    {
        return $this->hasMany(RegistrationRequest::class);
    }

    // Scopes
    public function scopeWithUser($query)
    {
        return $query->whereNotNull('user_id');
    }

    public function hasCitizenCard()
    {
        return $this->citizenCard() ? true : false;
    }
}
