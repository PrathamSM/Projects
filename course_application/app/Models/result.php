<?php

namespace App\Models;
use App\Models\User;
use Illuminate\Database\Eloquent\Model;

class result extends Model
{
    protected $fillable = ['user_id', 'quiz_id', 'score', 'attempted_at'];

    public function user() {
        return $this->belongsTo(User::class, 'user_id', 'id');
    }

    public function quiz() {
        return $this->belongsTo(Quiz::class);
    }
}
