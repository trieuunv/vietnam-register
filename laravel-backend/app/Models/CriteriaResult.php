<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CriteriaResult extends Model
{
    use HasFactory;

    protected $fillable = [
        'inspection_id',
        'criteria_id',
        'result',
        'notes',
        'inspector_id'
    ];

    public function inspection()
    {
        return $this->belongsTo(Inspection::class);
    }

    public function criteria()
    {
        return $this->belongsTo(InspectionCriteria::class, 'criteria_id');
    }

    public function inspector()
    {
        return $this->belongsTo(User::class, 'inspector_id');
    }

    // Helpers
    public function isPassed()
    {
        return $this->result === 'passed';
    }
}
