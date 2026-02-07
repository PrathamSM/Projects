<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
@extends('layouts.app')

@section('content')
<div class="bg-white p-6 rounded-lg shadow-md">
    <h1 class="text-xl font-bold mb-4">Available Quizzes</h1>
    <a href="{{ route('quizzes.create') }}" class="bg-blue-500 text-white px-4 py-2 rounded mb-4 inline-block">Create Quiz</a>
    <ul>
        @foreach ($quizzes as $quiz)
            <li class="mb-2">
                <a href="{{ route('quizzes.attempt', $quiz) }}" class="text-blue-500">{{ $quiz->title }}</a>
                <span>({{ $quiz->course->name }})</span>
            </li>
        @endforeach
    </ul>
</div>
@endsection

</body>
</html>