<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Center extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'name',
        'code',
        'phone',
        'email',
        'director_name',
        'status'
    ];

    public function address()
    {
        return $this->hasOne(CenterAddress::class);
    }

    public function inspections()
    {
        return $this->hasMany(Inspection::class);
    }

    public function appointments()
    {
        return $this->hasMany(Appointment::class);
    }

    public function inspectors()
    {
        return $this->hasMany(User::class, 'center_id')->where('center_role', 'inspector');
    }

    public function staff()
    {
        return $this->hasMany(User::class, 'center_id')->where('center_role', 'staff');
    }

    // Scopes
    public function scopeActive($query)
    {
        return $query->where('status', 'active');
    }
}
