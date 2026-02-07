<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enrolled Courses</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-8">
        <div class="mb-6 mt-3 flex w-full justify-between gap-4 border-b border-slate-200 pb-6">
            <h1 class="text-2xl font-bold">Enrolled Courses</h1>
            <div class="ml-auto flex gap-4">
                <a href="{{ route('user.courses.index') }}" class="py-2 px-4 bg-blue-400 text-white rounded-md hover:bg-blue-500 transition duration-300">All Courses</a>
                <a href="{{ route('dashboard.index') }}" class="py-2 px-4 bg-blue-400 text-white rounded-md hover:bg-blue-500 transition duration-300">Dashboard</a>
            </div>
        </div>
 
 
 
 
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            @if ($courses->isEmpty())
                <p>No enrolled courses available.</p>
            @else
                @foreach ($courses as $course)
                    <a href="{{ route('user.courses.show', $course->id) }}" class="block bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
                        <h2 class="text-xl font-bold mb-2">{{ $course->title }}</h2>
                        <p class="text-gray-700 mb-4">{{ $course->instructor_name }}</p>
                    </a>
                @endforeach
            @endif
        </div>
    </div>
</body>
</html>