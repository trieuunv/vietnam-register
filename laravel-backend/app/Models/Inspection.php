<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Inspection extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'vehicle_id',
        'center_id',
        'inspector_id',
        'inspection_date',
        'next_inspection_date',
        'certificate_number',
        'result',
        'notes',
        'fee',
        'mileage',
        'status'
    ];

    protected $dates = ['inspection_date', 'next_inspection_date'];

    public function vehicle()
    {
        return $this->belongsTo(Vehicle::class);
    }

    public function center()
    {
        return $this->belongsTo(Center::class);
    }

    public function inspector()
    {
        return $this->belongsTo(User::class, 'inspector_id');
    }

    public function criteriaResults()
    {
        return $this->hasMany(CriteriaResult::class);
    }

    public function payment()
    {
        return $this->hasOne(Payment::class);
    }

    public function appointment()
    {
        return $this->hasOne(Appointment::class);
    }

    public function owner() {}

    // Scopes
    public function scopePassed($query)
    {
        return $query->where('result', 'passed');
    }

    public function scopeFailed($query)
    {
        return $query->where('result', 'failed');
    }

    public function scopeCompleted($query)
    {
        return $query->where('status', 'completed');
    }

    // Helpers
    public function hasPassed()
    {
        return $this->result === 'passed';
    }

    public function getFailedCriteria()
    {
        return $this->results()->where('result', 'failed')->with('criteria')->get();
    }
}
