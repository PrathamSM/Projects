<?php

namespace App\Http\Controllers;
use App\Models\Option;
use App\Models\Question;
use Illuminate\Http\Request;

class OptionController extends Controller
{
   // Store options for a question
   public function store(Request $request, Question $question)
   {
       $request->validate([
           'options' => 'required|array|min:4', // At least 2 options are required
           'options.*.text' => 'required|string',
           'options.*.is_correct' => 'boolean',
       ]);

       foreach ($request->options as $option) {
           Option::create([
               'question_id' => $question->id,
               'option_text' => $option['text'],
               'is_correct' => $option['is_correct'] ?? false,
           ]);
       }

       return redirect()->route('questions.create', $question->quiz)
                        ->with('success', 'Options added successfully!');
   }
}
