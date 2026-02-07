<?php

namespace App\Models;
use App\Models\Course;
use App\Models\Question;
use Illuminate\Database\Eloquent\Model;

class quiz extends Model
{
    protected $fillable = ['title', 'course_id'];

    public function questions()
    {
        return $this->hasMany(Question::class);
    }

    public function course()
    {
        return $this->belongsTo(Course::class);
    }

    public function results()
    {
        return $this->hasMany(Result::class);
    }
}
