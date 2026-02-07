<?php

namespace App\Http\Controllers;
use App\Models\Quiz;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class QuestionController extends Controller
{
    //this function helps in to create questions returning questions(create.blade file)
    public function create(Quiz $quiz)
    {
        return view('questions.create', compact('quiz'));
    }


    //this function stores the questions for the particular quiz
    public function store(Request $request, Quiz $quiz)
{
    // Validate the request data
    $validated = $request->validate([
        'question_text' => 'required|string',
        'options' => 'required|array|min:4',
        'options.*.text' => 'required|string',
        'options.*.is_correct' => 'nullable|boolean', // Check for correct flag
    ]);

    // Create the question and associate it with the quiz
    $question = $quiz->questions()->create([
        'question_text' => $validated['question_text'],
    ]);

    // Save each option for the question
    foreach ($validated['options'] as $optionData) {
        $question->options()->create([
            'option_text' => $optionData['text'],
            'is_correct' => isset($optionData['is_correct']) ? 1 : 0, // Store the correct option
        ]);
    }

    // Redirect back to the quiz question creation page
    return redirect()->route('questions.create', ['quiz' => $quiz->id])
                     ->with('success', 'Question added successfully!');
}
}
