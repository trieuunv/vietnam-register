<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class CenterAddress extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'center_id',
        'province_code',
        'district_code',
        'ward_code',
        'detail'
    ];

    public function center()
    {
        return $this->belongsTo(Center::class);
    }
}
