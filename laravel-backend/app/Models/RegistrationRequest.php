<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class RegistrationRequest extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'owner_id',
        'vehicle_id',
        'request_type',
        'status',
        'rejection_reason',
        'submitted_at',
        'processed_at',
        'processed_by'
    ];

    protected $dates = ['submitted_at', 'processed_at'];

    public function owner()
    {
        return $this->belongsTo(Owner::class);
    }

    public function vehicle()
    {
        return $this->belongsTo(Vehicle::class);
    }

    public function processedBy()
    {
        return $this->belongsTo(User::class, 'processed_by');
    }

    // Scopes
    public function scopePending($query)
    {
        return $query->where('status', 'pending');
    }

    public function scopeApproved($query)
    {
        return $query->where('status', 'approved');
    }

    public function scopeRejected($query)
    {
        return $query->where('status', 'rejected');
    }

    // Helpers
    public function isProcessed()
    {
        return in_array($this->status, ['approved', 'rejected']);
    }
}
