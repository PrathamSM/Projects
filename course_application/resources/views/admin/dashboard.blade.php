{{-- <h1>Admin Panel</h1>

<form method="POST" action="{{route('logout')}}">
    @csrf
    <button type="submit">Logout</button>
</form> --}}



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 font-sans leading-normal tracking-normal">
 
    <!-- Header -->
    <header class="bg-white shadow-md">
        <div class="container mx-auto px-4 py-4 flex justify-between items-center">
            <h1 class="text-2xl font-bold text-blue-500">Admin Dashboard</h1>
            <div class="flex items-center space-x-4">
                <span class="text-gray-800 font-medium">Hello, {{ auth()->user()->name }}</span>
                <form method="POST" action="{{ route('logout') }}" class="inline">
                    @csrf
                    <button
                        type="submit"
                        class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-300">
                        Logout
                    </button>
                </form>
            </div>
        </div>
    </header>
 
    <!-- Main Content -->
    <div class="container mx-auto px-4 py-8">
        <!-- Stats Section -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
            <div class="bg-blue-100 p-4 rounded-lg shadow-md">
                <p class="text-xl font-semibold text-blue-800">Total Courses</p>
                <p class="text-3xl font-bold text-blue-900">{{$courseCount}}</p>
            </div>
            <div class="bg-green-100 p-4 rounded-lg shadow-md">
                <p class="text-xl font-semibold text-green-800">Total Enrollments</p>
                <p class="text-3xl font-bold text-green-900">{{$enrollmentCount}}</p>
            </div>
            <!-- <div class="bg-yellow-100 p-4 rounded-lg shadow-md">
                <p class="text-xl font-semibold text-yellow-800">Average Quiz Score</p>
                <p class="text-3xl font-bold text-yellow-900">80%</p>
            </div> -->
        </div>
 
        <!-- Quiz Statistics -->
        <div class="bg-white shadow-md rounded-lg p-6 mb-6">
    <h2 class="text-2xl font-semibold text-gray-800 mb-4">Quiz Statistics</h2>
    <ul class="list-disc list-inside space-y-2 text-gray-600">
        @forelse ($quizzes as $quiz)
            <li>{{ $quiz['title'] }}: Average Score - {{ $quiz['averageScore'] }}%</li>
        @empty
            <li>No quizzes available.</li>
        @endforelse
    </ul>
</div>


 
        <!-- Actions -->
        <div class="flex space-x-4">
            <a
                href="{{route('courses.create')}}"
                class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300">
                Create Course
            </a>
            <a
                href="{{route('courses.index')}}"
                class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-blue-300">
                View Courses
            </a>
            {{-- <button
                onclick="window.location.href='/create-test'"
                class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-300">
                Create Test
            </button> --}}
        </div>
    </div>
 
</body>
</html>