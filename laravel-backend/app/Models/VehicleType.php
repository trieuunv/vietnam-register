<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class VehicleType extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'name',
        'description',
        'inspection_frequency_months',
        'inspection_fee'
    ];

    public function vehicles()
    {
        return $this->hasMany(Vehicle::class);
    }

    public function inspectionCriteria()
    {
        return $this->hasMany(InspectionCriteria::class);
    }

    // Scopes
    public function scopeWithMandatoryCriteria($query)
    {
        return $query->with(['inspectionCriteria' => function($q) {
            $q->where('is_mandatory', true);
        }]);
    }
}
