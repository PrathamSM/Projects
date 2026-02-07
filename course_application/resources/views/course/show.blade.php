<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Details</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-r from-blue-100 via-blue-200 to-purple-300">
    <div class="container mx-auto p-8">
        <h1 class="text-4xl font-semibold text-center text-gray-800 mb-8">Course Details</h1>
       
        <div class="bg-white p-8 rounded-lg shadow-2xl transform hover:scale-105 transition-all duration-300 ease-in-out">
            <h2 class="text-3xl font-bold mb-4 text-gray-800">{{ $course->title }}</h2>
            <p class="mb-4 text-gray-700">{{ $course->description }}</p>
            <p><strong class="mb-4 text-gray-700">Instructor:</strong> <span class="mb-4 text-gray-1100">{{ $course->instructor_name }}</span></p>
           
            <div class="mb-6">
               
                @php
                    $pdfFiles = array_filter($course->content_files, function($file) {
                        return $file['type'] === 'pdf';
                    });
                @endphp
                @if (!empty($pdfFiles))
                <h3 class="text-2xl font-semibold mb-2 text-gray-800">PDF Resources</h3>
                    <ul class="list-disc list-inside pl-4">
                        @foreach ($pdfFiles as $file)
                            <li><a href="{{ asset('storage/' . $file['path']) }}" target="_blank" class="text-blue-600 hover:underline">{{ basename($file['path']) }}</a></li>
                        @endforeach
                    </ul>
                @else
                    <p class="text-gray-700">No PDF resources available.</p>
                @endif
            </div>
           
            <div class="mb-6">
               
                @php
                    $videoFiles = array_filter($course->content_files, function($file) {
                        return $file['type'] === 'video';
                    });
                @endphp
                @if (!empty($videoFiles))
                <h3 class="text-2xl font-semibold mb-2 text-gray-800">Video Resources</h3>
                    <ul class="list-disc list-inside pl-4">
                        @foreach ($videoFiles as $file)
                            {{-- @if ($file['link'] != null)  --}}
                            <li><a href="{{ $file['link'] }}" target="_blank" class="text-blue-600 hover:underline">Watch Video</a></li>
                               
                            {{-- @endif --}}
                        @endforeach
                    </ul>
                @else
                    <p class="text-gray-700">No video resources available.</p>
                @endif
            </div>
 
            <div class="mt-6 text-center">
    <a href="{{ route('quizzes.create', ['course_id' => $course->id]) }}">
        <button class="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded">
            create Quiz
        </button>
    </a>
</div>
        </div>
    </div>
</body>
</html>