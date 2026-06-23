<?php

namespace App\Models;

// use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;
use Tymon\JWTAuth\Contracts\JWTSubject;

class User extends Authenticatable implements JWTSubject
{
    use HasApiTokens, HasFactory, Notifiable, SoftDeletes;

    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    public function getJWTCustomClaims()
    {
        return [];
    }

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'username',
        'password',
        'email',
        'phone',
        'full_name',
        'role',
        'status',
        'center_id',
        'center_role',
        'center_permissions',
    ];

    /**
     * The attributes that should be hidden for serialization.
     *  
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function owner()
    {
        return $this->hasOne(Owner::class);
    }

    public function address()
    {
        return $this->hasOne(UserAddress::class);
    }

    public function inspections()
    {
        return $this->hasMany(Inspection::class, 'inspector_id');
    }

    public function criteriaResults()
    {
        return $this->hasMany(CriteriaResult::class, 'inspector_id');
    }

    public function processedRequests()
    {
        return $this->hasMany(RegistrationRequest::class, 'processed_by');
    }

    public function notifications()
    {
        return $this->hasMany(Notification::class);
    }

    // Staff
    public function center()
    {
        return $this->belongsTo(Center::class, 'center_id');
    }

    // Scopes
    public function scopeStaffs($query)
    {
        return $query->where('role', 'staff');
    }

    public function scopeInspectors($query)
    {
        return $query->where('role', 'inspector');
    }

    public function scopeAdmins($query)
    {
        return $query->where('role', 'admin');
    }

    public function scopeActive($query)
    {
        return $query->where('status', 'active');
    }
}
