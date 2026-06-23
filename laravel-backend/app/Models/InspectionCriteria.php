<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class InspectionCriteria extends Model
{
    use HasFactory, SoftDeletes;

    protected $table = 'inspection_criteria';

    protected $fillable = [
        'vehicle_type_id',
        'name',
        'description',
        'standard',
        'is_mandatory'
    ];

    public function vehicleType()
    {
        return $this->belongsTo(VehicleType::class);
    }

    public function criteriaResults()
    {
        return $this->hasMany(CriteriaResult::class, 'criteria_id');
    }

    // Scopes
    public function scopeMandatory($query)
    {
        return $query->where('is_mandatory', true);
    }

    public function scopeOptional($query)
    {
        return $query->where('is_mandatory', false);
    }
}
