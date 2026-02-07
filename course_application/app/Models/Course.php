<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Course extends Model
{
        use HasFactory;

        protected $fillable = ['title', 'description', 'instructor_name', 'content_files'];

        protected $casts = [
            'content_files' => 'array', // Cast to array
        ];



        public function enrollments(){
            return $this->hasMany(Enrollment::class);
        }
}