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
<div class="container">
    <h1>{{ $quiz->title }}</h1>

    <h2>Questions</h2>
    @if($quiz->questions->isEmpty())
        <p>No questions added yet.</p>
    @else
        @foreach($quiz->questions as $question)
            <div class="mb-3">
                <strong>{{ $loop->iteration }}. {{ $question->text }}</strong>
                <ul>
                    @foreach($question->options as $option)
                        <li>{{ $option->text }} 
                            @if($option->is_correct)
                                <strong>(Correct Answer)</strong>
                            @endif
                        </li>
                    @endforeach
                </ul>
            </div>
        @endforeach
    @endif
</div>
@endsection

</body>
</html>