<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Courses</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-8">
        @if (session('success'))
    <div class="bg-green-100 text-green-700 p-4 rounded-md mb-6 relative">
        {{ session('success') }}
        <button onclick="this.parentElement.style.display='none'" class="absolute top-0 right-0 mt-2 mr-2 text-green-700 hover:text-green-900">
            &times;
        </button>
    </div>
@endif
 
     
 
 
        <div class="mb-6 mt-3 flex w-full justify-between gap-4 border-b border-slate-200 pb-6">
            <a href="{{route('courses.index')}}"><h1 class="text-2xl font-bold">All Courses</h1></a>
 
            <form action="{{ route('courses.index') }}" method="GET" class="flex gap-2">
                <input type="text" name="search" placeholder="Search courses..." class="px-4 py-2 border rounded" value="{{request('search')}}">
                <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Search</button>
            </form>
 
            <a href="{{ route('admin.dashboard') }}" class="bg-blue-500 text-white px-4 py-2 h-10 rounded">Dashboard</a>
        </div>
 
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            @if ($courses->isEmpty())
                <p>No courses available.</p>
            @else
                @foreach ($courses as $course)
                    <div class="bg-white p-6 rounded-lg shadow-md hover:shadow-2xl transition-shadow duration-300">
                        <h2 class="text-xl font-bold mb-2">{{ $course->title }}</h2>
                        <p class="text-gray-700 mb-4">{{ $course->instructor_name }}</p>
                        <div class="flex gap-5">
                            <a href="{{ route('courses.show', $course->id) }}" class="bg-blue-500 text-white px-4 py-2 rounded mt-2">View</a>
                            <a href="{{ route('courses.edit', $course->id) }}" class="bg-green-500 text-white px-4 py-2 rounded mt-2">Edit</a>
                            <form action="{{ route('courses.destroy', $course->id) }}" method="POST" class="inline-block">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded mt-2">Delete</button>
                            </form>
                        </div>
                    </div>
                @endforeach
            @endif
        </div>
        <divclass="mt-6"> {{$courses->links()}}</div>
    </div>
</body>
</html>