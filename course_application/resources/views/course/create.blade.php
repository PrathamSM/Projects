<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Course</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-r from-blue-100 via-blue-200 to-purple-300">
 
    <div class="flex justify-center items-center min-h-screen">
        <div class="w-full max-w-2xl bg-white p-8 rounded-lg shadow-2xl transform hover:scale-105 transition-all duration-300 ease-in-out">
            <div class="flex justify-between items-center mb-8">
                <a href="javascript:history.back()" class="text-blue-600 hover:underline">&larr; Back</a>
                <h1 class="text-4xl font-semibold text-gray-800 flex-grow text-center">Create Course</h1>
                <div class="w-16"></div>
            </div>  
 
            @if (session('success'))
    <div class="bg-green-100 text-green-700 p-4 rounded-md mb-6 relative">
        {{ session('success') }}
        <button onclick="this.parentElement.style.display='none'" class="absolute top-0 right-0 mt-2 mr-2 text-green-700 hover:text-green-900">
            &times;
        </button>
    </div>
@endif
 
            <form action="{{ route('courses.store') }}" method="POST" enctype="multipart/form-data">
                @csrf
                <div class="space-y-6">
 
                    <!-- Course Title -->
                    <div>
                        <label for="title" class="block text-lg font-medium text-gray-800">Course Title</label>
                        <input type="text" id="title" name="title" value="{{old('title')}}" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('title') border-red-500 @enderror">
                        @error('title')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- Course Description -->
                    <div>
                        <label for="description" class="block text-lg font-medium text-gray-800">Course Description</label>
                        <textarea id="description" name="description" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('description') border-red-500 @enderror" rows="4">{{old('title')}}</textarea>
                        @error('description')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- Instructor Name -->
                    <div>
                        <label for="instructor_name" class="block text-lg font-medium text-gray-800">Instructor Name</label>
                        <input type="text" id="instructor_name" name="instructor_name" value="{{old('instructor_name')}}" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('instructor_name') border-red-500 @enderror">
                        @error('instructor_name')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- PDF Files -->
                    <div>
                        <label for="pdf_files" class="block text-lg font-medium text-gray-800">Upload PDF Files</label>
                        <input type="file" id="pdf_files" name="pdf_files[]" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('pdf_files.*') border-red-500 @enderror" multiple>
                        @error('pdf_files.*')
                        <p class="text-red-500 text-sm mt-1">File type must be pdf</p>
                        @enderror
                    </div>
 
                    <!-- Video Links -->
                    <div>
                        <label for="video_links" class="block text-lg font-medium text-gray-800">Video Links</label>
                        <input type="url" id="video_links" value="{{old('video_links[]')}}" name="video_links[]" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('video_links.*') border-red-500 @enderror">
                        @error('video_links.*')
                        <p class="text-red-500 text-sm mt-1">The Video link must be a valid URL</p>
                        @enderror
                    </div>
                    <!-- Submit Button -->
                    <div>
                        <button type="submit" class="w-full py-3 px-6 bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold rounded-md hover:from-blue-600 hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300">
                            Create Course
                        </button>
                    </div>
 
                </div>
            </form>
        </div>
    </div>
</body>