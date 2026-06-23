<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Appointment extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'owner_id',
        'created_by',
        'vehicle_id',
        'center_id',
        'appointment_date',
        'status',
        'confirmation_code',
        'notes',
        'inspection_id'
    ];

    protected $dates = ['appointment_date'];

    public function vehicle()
    {
        return $this->belongsTo(Vehicle::class);
    }

    public function center()
    {
        return $this->belongsTo(Center::class);
    }

    public function inspection()
    {
        return $this->belongsTo(Inspection::class);
    }

    public function owner()
    {
        return $this->belongsTo(Owner::class, 'owner_id');
    }

    // Scopes
    public function scopeUpcoming($query)
    {
        return $query->where('appointment_date', '>', now())
            ->where('status', 'confirmed');
    }

    public function scopePending($query)
    {
        return $query->where('status', 'pending');
    }

    // Helpers
    public function isCompleted()
    {
        return $this->status === 'completed';
    }

    public function isCancelled()
    {
        return $this->status === 'cancelled';
    }
}
