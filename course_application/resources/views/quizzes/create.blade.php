<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Quiz</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 min-h-screen flex items-center justify-center">

@extends('layouts.app')
`
@section('title', 'Create Quiz')

@section('content')
<div class="bg-white p-6 rounded-lg shadow-md w-full max-w-md">
    <h1 class="text-2xl font-bold text-gray-700 mb-4 text-center">Create a New Quiz</h1>

    <!-- Form to create the quiz -->
    <form method="POST" action="{{ route('quizzes.store') }}" class="space-y-4">
        @csrf
        <input type="hidden" name="course_id" value="{{ request('course_id') }}">

        <!-- Quiz Title -->
        <div>
            <label for="title" class="block text-gray-600 font-medium">Quiz Title:</label>
            <input type="text" name="title" id="title" required 
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400">
        </div>

        <!-- Submit Button -->
        <div class="text-center">
            <button type="submit" 
                    class="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-6 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-purple-400">
                Create Quiz
            </button>
        </div>
    </form>
</div>

@endsection

</body>
</html>
