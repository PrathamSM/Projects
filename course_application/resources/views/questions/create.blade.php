<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <title>Add Questions</title>
</head>
<body class="bg-gray-100 min-h-screen flex flex-col items-center">

@extends('layouts.app')

@section('content')
<div class="w-full max-w-3xl mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
    <h1 class="text-2xl font-bold text-gray-700 mb-4">Add Questions to Quiz: <span class="text-purple-500">{{ $quiz->title }}</span></h1>

    @if(session('success'))
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-2 rounded relative mb-4">
            {{ session('success') }}
        </div>
    @endif

    <form method="POST" action="{{ route('questions.store', ['quiz' => $quiz->id]) }}" class="space-y-6">
        @csrf

        <!-- Question Text -->
        <div>
            <label for="question_text" class="block text-gray-600 font-medium">Question Text</label>
            <input 
                type="text" 
                id="question_text" 
                name="question_text" 
                required 
                placeholder="Enter question text" 
                class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
            >
        </div>

        <!-- Options -->
        <div id="options">
            <label class="block text-gray-600 font-medium">Options</label>
            @for($i = 0; $i < 4; $i++)
                <div class="flex items-center space-x-4 mt-2">
                    <input 
                        type="text" 
                        name="options[{{ $i }}][text]" 
                        required 
                        placeholder="Option {{ $i + 1 }}" 
                        class="flex-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
                    >
                    <label class="flex items-center">
                        <input 
                            type="checkbox" 
                            name="options[{{ $i }}][is_correct]" 
                            value="1" 
                            class="mr-2">
                        <span class="text-gray-600">Correct</span>
                    </label>
                    
                </div>
            @endfor
        </div>

        <!-- Add Question Button -->
        <div class="text-right">
            <button 
                type="submit" 
                class="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-6 rounded-lg shadow-md">
                Add Question
            </button>
        </div>
    </form>

    <!-- Back to Courses Button -->
    <div class="mt-6 text-center">
        <a href="{{ route('courses.index') }}">
            <button class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded-lg">
                Back to Courses
            </button>
        </a>
    </div>
</div>
@endsection

</body>
</html>
