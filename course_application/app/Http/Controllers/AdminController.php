<?php
 
namespace App\Http\Controllers;
 
use App\Http\Controllers\Controller;
use App\Models\Course;
use App\Models\Enrollment;
use App\Models\quiz;
use Illuminate\Support\Facades\Log;

class AdminController extends Controller
{
 
 
    public static function getCourseCount()
    {
        $cnt = Course::count();
        return $cnt;
    }
 
 
    public static function getTotalEnrollments(){
        $enrollmentCnt = Enrollment::count();
        return $enrollmentCnt;
    }

    public function getQuizInfo()
    {
        // Fetch all quizzes with their average scores
        $quizzes = quiz::with('results') // Eager load the results for each quiz
            ->get()
            ->map(function ($quiz) {
                // Calculate the average score
                $averageScore = $quiz->results->avg('score'); // Using Eloquent to calculate average
                
                // Prepare and return quiz data with formatted average score
                return [
                    'title' => $quiz->title,
                    'averageScore' => $averageScore ? round($averageScore, 2) : 0, // Round to 2 decimal places
                ];
            });
    
        // Log quiz titles and average scores for debugging
        Log::info('Quiz Statistics:');
        foreach ($quizzes as $quiz) {
            Log::info("Quiz: {$quiz['title']}, Average Score: {$quiz['averageScore']}");
        }
    
        // Pass the data to the view
        return view('admin.dashboard', compact('quizzes'));
    }
    
 
}