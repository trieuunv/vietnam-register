<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'title',
        'content',
        'notification_type',
        'is_read',
        'related_id',
        'related_type'
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    // Scopes
    public function scopeUnread($query)
    {
        return $query->where('is_read', false);
    }

    public function scopeRecent($query, $days = 7)
    {
        return $query->where('created_at', '>=', now()->subDays($days));
    }

    // Helpers
    public function markAsRead()
    {
        $this->update(['is_read' => true]);
    }

    public function getRelatedModel()
    {
        if (!$this->related_type || !$this->related_id) {
            return null;
        }

        $modelClass = 'App\\Models\\' . ucfirst($this->related_type);
        
        if (class_exists($modelClass)) {
            return $modelClass::find($this->related_id);
        }

        return null;
    }
}
