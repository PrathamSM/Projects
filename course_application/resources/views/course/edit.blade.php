<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Course</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-r from-blue-100 via-blue-200 to-purple-300">
    <div class="flex justify-center items-center min-h-screen">
        <div class="w-full max-w-2xl bg-white p-8 rounded-lg shadow-2xl transform hover:scale-105 transition-all duration-300 ease-in-out">
            <div class="flex justify-between items-center mb-8">
                <a href="javascript:history.back()" class="text-blue-600 hover:underline">&larr; Back</a>
                <h1 class="text-4xl font-semibold text-gray-800 flex-grow text-center">Edit Course</h1>
                <div class="w-16"></div>
            </div>  
           
            @if (session('success'))
                <div class="bg-green-100 text-green-700 p-4 rounded-md mb-6">
                    {{ session('success') }}
                </div>
            @endif
 
            <form action="{{ route('courses.update', $course->id) }}" method="POST" enctype="multipart/form-data">
                @csrf
                @method('PUT')
                <div class="space-y-6">
                    <!-- Course Title -->
                    <div>
                        <label for="title" class="block text-lg font-medium text-gray-800">Course Title</label>
                        <input type="text" id="title" name="title" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('title') border-red-500 @enderror" value="{{ $course->title }}">
                        @error('title')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- Course Description -->
                    <div>
                        <label for="description" class="block text-lg font-medium text-gray-800">Description</label>
                        <textarea id="description" name="description" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('description') border-red-500 @enderror" rows="4" >{{ $course->description }}</textarea>
                        @error('description')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- Instructor Name -->
                    <div>
                        <label for="instructor_name" class="block text-lg font-medium text-gray-800">Instructor Name</label>
                        <input type="text" id="instructor_name" name="instructor_name" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('instructor_name') border-red-500 @enderror" value="{{ $course->instructor_name }}">
                        @error('instructor_name')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                        @enderror
                    </div>
 
                    <!-- Existing PDF Files -->
                    <div>
                        @if ($course->content_files)
                        <label class="block text-lg font-medium text-gray-800">Existing PDF Files</label>
                           
                        @endif
                        @foreach ($course->content_files as $file)
                            @if (isset($file['path']) && $file['type'] === 'pdf')
                                <div class="flex items-center mb-2">
                                    <input type="checkbox" name="remove_pdfs[]" value="{{ $file['path'] }}" class="mr-2">
                                    <a href="{{ asset('storage/' . $file['path']) }}" target="_blank">{{ basename($file['path']) }}</a>
                                </div>
                            @endif
                        @endforeach
                    </div>
 
                    <!-- Add New PDF Files -->
                    <div>
                        <label for="pdf_files" class="block text-lg font-medium text-gray-800">Add New PDF Files</label>
                        <input type="file" id="pdf_files" name="pdf_files[]" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('pdf_files.*') border-red-500 @enderror" multiple>
                        @error('pdf_files.*')
                        <p class="text-red-500 text-sm mt-1">File type must be pdf</p>
                        @enderror
                    </div>
 
                    <!-- Existing Video Links -->
                    <div>
                        @if ($course->content_files)
                        <label class="block text-lg font-medium text-gray-800">Existing Video Links</label>
                        @endif
                        @foreach ($course->content_files as $file)
                            @if (isset($file['link']) && $file['type'] === 'video')
                                <div class="flex items-center mb-2">
                                    <input type="checkbox" name="remove_videos[]" value="{{ $file['link'] }}" class="mr-2">
                                    <a href="{{ $file['link'] }}" target="_blank">{{ $file['link'] }}</a>
                                </div>
                            @endif
                        @endforeach
                    </div>
 
                    <!-- Add New Video Links -->
                    <div>
                        <label for="video_links" class="block text-lg font-medium text-gray-800">Add New Video Links</label>
                        <textarea id="video_links" name="video_links" class="w-full p-4 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition duration-300 @error('video_links') border-red-500 @enderror">{{old('video_links')}}</textarea>
                        @error('video_links')
                        <p class="text-red-500 text-sm mt-1">{{$message}}</p>
                        @enderror
                    </div>
 
                    <!-- Submit Button -->
                    <div>
                        <button type="submit" class="w-full py-3 px-6 bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold rounded-md hover:from-blue-600 hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300" placeholder="Enter each URL on a new line">
                            Update Course
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
 
    <script>
        document.getElementById('video_links').addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                this.value += '\n';
            }
        });
    </script>
</body>
</html>