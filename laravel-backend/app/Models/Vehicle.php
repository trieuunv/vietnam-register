<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Vehicle extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'owner_id',
        'vehicle_type_id',
        'registration_number',
        'chassis_number',
        'engine_number',
        'brand',
        'model',
        'manufacture_year',
        'color',
        'seat_count',
        'first_registration_date',
        'purpose_of_use',
        'registration_status',
        'status'
    ];

    protected $dates = ['first_registration_date'];

    public function owner()
    {
        return $this->belongsTo(Owner::class);
    }

    public function vehicleType()
    {
        return $this->belongsTo(VehicleType::class);
    }

    public function registrationRequests()
    {
        return $this->hasMany(RegistrationRequest::class);
    }

    public function inspections()
    {
        return $this->hasMany(Inspection::class);
    }

    public function appointments()
    {
        return $this->hasMany(Appointment::class);
    }

    public function latestInspection()
    {
        return $this->hasOne(Inspection::class)->latestOfMany();
    }

    public function latestPassedInspection()
    {
        return $this->hasOne(Inspection::class)->whereIn('result', ['passed', 'conditional'])->latest();
    }


    // Scopes
    public function scopeRegistered($query)
    {
        return $query->where('registration_status', 'registered');
    }

    public function scopeActive($query)
    {
        return $query->where('status', 'active');
    }

    // Helpers
    public function needsInspection()
    {
        if (!$this->latestInspection) {
            return true;
        }

        $nextInspectionDate = $this->latestInspection->next_inspection_date;
        return now()->greaterThanOrEqualTo($nextInspectionDate);
    }
}
