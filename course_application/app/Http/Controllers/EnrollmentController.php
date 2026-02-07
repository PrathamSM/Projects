<?php

namespace App\Http\Controllers;

use App\Models\Course;
use App\Models\Enrollment;
use App\Notifications\EnrollmentSuccessNotification;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\User;




class EnrollmentController extends Controller
{
    // public function show($courseId)
    // {
    //     $course = Course::findOrFail($courseId);
    //     $isAdmin = Auth::check() && Auth::user()->is_admin;

    //     return view('courses.show', compact('course', 'isAdmin'));
    // }

    // public function enroll($courseId)
    // {
    //     if (!Auth::check()) {
    //         return redirect()->route('login')->withErrors('You need to log in to enroll.');
    //     }

    //     $user = Auth::user();
    //     $existingEnrollment = Enrollment::where('user_id', $user->id)
    //                                     ->where('course_id', $courseId)
    //                                     ->first();

    //     if ($existingEnrollment) {
    //         return redirect()->back()->with('message', 'You are already enrolled in this course.');
    //     }

    //     Enrollment::create([
    //         'user_id' => $user->id,
    //         'course_id' => $courseId,
    //     ]);

    //     // $user->notify(new EnrollmentSuccessNotification($course));

    //     return redirect()->back()->with('message', 'Enrolled successfully! A confirmation email has been sent.');
    // } 

   public function enroll(Request $request, $courseId) {
    $userId = Auth::id();
   $user = User::find($request->user_id);
   $course = Course::find($request->course_id);

    $existingEnrollment = Enrollment::where('user_id', $userId)->where('course_id', $courseId)->first();


    if(!$existingEnrollment) {
        Enrollment::create([
            'user_id' => $userId,
            'course_id' => $courseId
        ]);
    }
    // $user = auth()->Auth::id();
    // $course = Course::findOrFail($courseId);
// $user->notify(new EnrollmentSuccessNotification($course));

    
    return redirect()->back()->with('success', 'You have enrolled successfully!');
   }
    
} 

