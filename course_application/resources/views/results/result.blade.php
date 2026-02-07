<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <title>Quiz Result</title>
</head>
<body>
@extends('layouts.app')

@section('content')
<div class="max-w-3xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
    <!-- Title Section -->
    <h1 class="text-3xl font-bold text-blue-600 mb-6 text-center">Quiz Result</h1>

    <!-- Quiz Information -->
    <div class="mb-8">
        <h2 class="text-xl font-semibold text-gray-800 mb-2">Quiz Title: <span class="text-blue-500">{{ $quiz->title }}</span></h2>
        <p class="text-lg text-gray-700">
            Your Score: 
            <span class="font-bold text-green-600">{{ $score }}</span>
        </p>
        <p class="text-gray-600 mt-1">
            You answered 
            <span class="font-bold">{{ $correctAnswers }}</span> 
            out of 
            <span class="font-bold">{{ $totalQuestions }}</span> questions correctly.
        </p>
    </div>

    <!-- Progress Bar -->
    <div class="w-full bg-gray-200 rounded-full h-4 mb-6">
        <div 
            class="bg-blue-500 h-4 rounded-full" 
            style="width: {{ ($correctAnswers / $totalQuestions) * 100 }}%; transition: width 0.5s;">
        </div>
    </div>

    <!-- Buttons -->
    <div class="flex justify-center gap-4">
        <a 
            href="{{ route('dashboard.index') }}" 
            class="bg-blue-500 hover:bg-blue-600 text-white font-semibold px-6 py-2 rounded-lg shadow-md">
            Back to Dashboard
        </a>
        <!-- <a 
            href="{{ route('quizzes.attempt', $quiz->id) }}" 
            class="bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold px-6 py-2 rounded-lg shadow-md">
            Retake Quiz
        </a> -->
    </div>
</div>
@endsection
</body>
</html>
