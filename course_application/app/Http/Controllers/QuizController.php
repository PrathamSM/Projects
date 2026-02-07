<?php

namespace App\Http\Controllers;

use App\Models\Course;
use App\Models\Quiz;
use App\Models\result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class QuizController extends Controller
{
    public function index()
    {
        $quizzes = Quiz::all(); // Retrieve all quizzes
        return view('quizzes.index', compact('quizzes'));
    }


    //this function takes course id and at the time of quiz creation we only have to input quiz title
    public function create(Request $request) {
        $courseId = $request->course_id;
        return view('quizzes.create', compact('courseId'));
    }
    
    
    //this function helps to store quiz info like title and course id
    public function store(Request $request)
    {
        $validated = $request->validate([
            'title' => 'required|string|max:255',
            'course_id' => 'required|integer|exists:courses,id', // Validate course_id
        ]);
    
        $quiz = Quiz::create($validated);
    
        return redirect()->route('questions.create', ['quiz' => $quiz->id])
                         ->with('success', 'Quiz created successfully!');
    }
    
    
    
    //this function helps in to show the quiz to the user
    public function show(Quiz $quiz)
    {
        // Load the quiz along with its questions and options
        $quiz->load('questions.options');
    
        return view('quizzes.attempt', compact('quiz'));
    }


//this function is created to attempt quiz
public function attemptQuiz($courseId)
{
    // Fetch the course and its quiz
    // $course = Course::findOrFail($courseId);
    $quiz = Quiz::where('course_id', $courseId)->first();

    if (!$quiz) {
        return redirect()->back()->with('error', 'No quiz found for this course.');
    }

    // Return the quiz attempt view
    return view('quizzes.attempt', compact('quiz'));
}


//this function stores the user result
public function storeResult(Request $request, $quizId)
{
    // Validate the incoming request
    $validated = $request->validate([
        'score' => 'required|integer',
    ]);

    // Check if a result record already exists for this user and quiz
    $existingResult = Result::where('user_id', Auth::id())
                            ->where('quiz_id', $quizId)
                            ->first();

    if ($existingResult) {
        // Update the existing result
        $existingResult->update([
            'score' => $validated['score'],
            'attempted_at' => now(),
        ]);
    } else {
        // Create a new result record
        Result::create([
            'user_id' => Auth::id(),
            'quiz_id' => $quizId,
            'score' => $validated['score'],
            'attempted_at' => now(),
        ]);
    }

    return redirect()->route('quizzes.show', $quizId)->with('success', 'Your result has been recorded.');
}



//this function is created for showing quiz info
public function stats()
    {
        // Fetch quizzes with their average scores
        $quizzes = Quiz::with('results')
            ->get()
            ->map(function ($quiz) {
                $averageScore = $quiz->results->avg('score');
                return [
                    'title' => $quiz->title,
                    'averageScore' => $averageScore ? round($averageScore, 2) : 0,
                ];
            });

        return view('admin.dashboard', compact('admin'));
    }

    
}
