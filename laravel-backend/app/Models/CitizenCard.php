<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class CitizenCard extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'owner_id',
        'citizen_id',
        'nationality',
        'place_of_origin',
        'date_of_issue',
        'place_of_issue',
        'image_url'
    ];

    protected $dates = ['date_of_issue'];

    public function owner()
    {
        return $this->belongsTo(Owner::class);
    }

    // Helpers
    public function getFormattedCitizenId()
    {
        return substr($this->citizen_id, 0, 3) . ' ' . 
               substr($this->citizen_id, 3, 3) . ' ' . 
               substr($this->citizen_id, 6);
    }
}
