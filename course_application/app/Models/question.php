<?php

namespace App\Models;
use App\Models\Option;
use App\Models\Quiz;
use Illuminate\Database\Eloquent\Model;

class question extends Model
{
    protected $fillable = ['quiz_id', 'question_text'];

    public function quiz()
    {
        return $this->belongsTo(Quiz::class);
    }

    public function options()
    {
        return $this->hasMany(Option::class);
    }
}
