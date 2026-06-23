<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Payment extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'inspection_id',
        'amount',
        'payment_method',
        'transaction_code',
        'payment_date',
        'status',
        'notes'
    ];

    protected $dates = ['payment_date'];

    public function inspection()
    {
        return $this->belongsTo(Inspection::class);
    }

    // Scopes
    public function scopeCompleted($query)
    {
        return $query->where('status', 'completed');
    }

    public function scopePending($query)
    {
        return $query->where('status', 'pending');
    }

    // Helpers
    public function isSuccessful()
    {
        return $this->status === 'completed';
    }

    public function getPaymentMethodName()
    {
        $methods = [
            'cash' => 'Tiền mặt',
            'credit_card' => 'Thẻ tín dụng',
            'bank_transfer' => 'Chuyển khoản',
            'ewallet' => 'Ví điện tử'
        ];
        
        return $methods[$this->payment_method] ?? $this->payment_method;
    }
}
