<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <title>{{ $quiz->title }} - Quiz</title>
</head>
<body>
@extends('layouts.app')

@section('content')
<div class="max-w-4xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
    <!-- Quiz Title -->
    <h1 class="text-3xl font-bold text-center text-blue-600 mb-6">{{ $quiz->title }}</h1>

    <!-- Form Start -->
    <form action="{{ route('quizzes.result.store', $quiz->id) }}" method="POST">
        @csrf

        <!-- Loop Through Questions -->
        @foreach ($quiz->questions as $index => $question)
        <div class="mb-8">
            <!-- Question Number and Text -->
            <p class="text-lg font-semibold mb-2">
                Question {{ $index + 1 }}: {{ $question->question_text }}
            </p>

            <!-- Options -->
            @foreach ($question->options as $option)
            <div class="ml-4 mb-2">
                <label class="flex items-center">
                    <input 
                        type="radio" 
                        name="answers[{{ $question->id }}]" 
                        value="{{ $option->id }}" 
                        required 
                        class="form-radio h-4 w-4 text-blue-500 border-gray-300 focus:ring-blue-400">
                    <span class="ml-2 text-gray-700">{{ $option->option_text }}</span>
                </label>
            </div>
            @endforeach
        </div>
        @endforeach

        <!-- Submit Button -->
        <div class="text-center">
            <button 
                type="submit" 
                class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-6 rounded-lg shadow-lg transition duration-300">
                Submit Quiz
            </button>
        </div>
    </form>
</div>
@endsection
</body>
</html>
