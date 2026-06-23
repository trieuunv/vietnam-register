<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class UserAddress extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'user_id',
        'province_code',
        'district_code',
        'ward_code',
        'detail'
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
