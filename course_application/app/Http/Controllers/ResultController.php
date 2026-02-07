<?php

namespace App\Http\Controllers;

use App\Models\Quiz;
use App\Models\result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth;

class ResultController extends Controller
{
    //this store function helps to store result of user after submission
    public function store(Request $request, Quiz $quiz)
{
    Log::info('Quiz Submission Started');

    $validated = $request->validate([
        'answers' => 'required|array',
        'answers.*' => 'required|exists:options,id',
    ]);

    $correctAnswers = 0;

    foreach ($quiz->questions as $question) {
        $selectedOptionId = $validated['answers'][$question->id] ?? null;

        $correctOption = $question->options()->where('is_correct', true)->first();
        if ($correctOption && $correctOption->id == $selectedOptionId) {
            $correctAnswers++;
        }
    }

    $totalQuestions = $quiz->questions->count();
    $score = round(($correctAnswers / $totalQuestions) * 100);

    // Save the result
    Log::info("Score calculated: {$score}");

    $result = Result::updateOrCreate(
        [
            'user_id' => Auth::id(),
            'quiz_id' => $quiz->id,
        ],
        [
            'score' => $score,
            'attempted_at' => now(),
        ]
    );

    Log::info('Result saved successfully', ['result_id' => $result->id]);

    // Redirect to the result view
    return view('results.result', [
        'quiz' => $quiz,
        'score' => $score,
        'correctAnswers' => $correctAnswers,
        'totalQuestions' => $totalQuestions,
    ]);
}


//this function helps to show quiz info in user dashboard
public function index()
    {
        // Fetch authenticated user
        $user = Auth::user();

        // Fetch the quiz results for the user
        // $results = Result::with('quiz')
        //     ->where('user_id', $user->id)
        //     ->orderBy('attempted_at', 'desc')
        //     ->get();
        $results = Result::with('quiz')
    ->where('user_id', $user->id)
    ->orderBy('attempted_at', 'desc')
    ->paginate(10);

        // Pass data to the view
        return view('dashboard.index', [
            'user' => $user,
            'results' => $results,
        ]);
    }
}
