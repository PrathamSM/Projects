<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
 
    <form method="POST" action="/register" class="bg-white shadow-lg rounded-lg p-8 max-w-md w-full">
        @csrf
        <h2 class="text-2xl font-bold text-gray-700 mb-6 text-center">Register</h2>
 
        <!-- Name Field -->
        <div class="mb-4">
            <label for="name" class="block text-sm font-medium text-gray-700">Name</label>
            <input
                type="text"
                id="name"
                name="name"
                placeholder="Enter your name"
                value="{{ old('name') }}"
                class="mt-1 block w-full px-4 py-2 border {{ $errors->has('name') ? 'border-red-500' : 'border-gray-300' }} rounded-lg focus:ring-blue-500 focus:border-blue-500">
            @error('name')
                <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
            @enderror
        </div>
 
     
 
        <!-- Email Field -->
        <div class="mb-4">
            <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
            <input
                type="email"
                id="email"
                name="email"
                placeholder="Enter your email"
                value="{{ old('email') }}"
                class="mt-1 block w-full px-4 py-2 border {{ $errors->has('email') ? 'border-red-500' : 'border-gray-300' }} rounded-lg focus:ring-blue-500 focus:border-blue-500">
            @error('email')
                <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
            @enderror
        </div>
 
        <!-- Password Field -->
        <div class="mb-4">
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input
                type="password"
                id="password"
                name="password"
                placeholder="Enter your password"
                class="mt-1 block w-full px-4 py-2 border {{ $errors->has('password') ? 'border-red-500' : 'border-gray-300' }} rounded-lg focus:ring-blue-500 focus:border-blue-500">
            @error('password')
                <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
            @enderror
        </div>
 
        <!-- Confirm Password Field -->
        <div class="mb-6">
            <label for="password_confirmation" class="block text-sm font-medium text-gray-700">Confirm Password</label>
            <input
                type="password"
                id="password_confirmation"
                name="password_confirmation"
                placeholder="Confirm your password"
                class="mt-1 block w-full px-4 py-2 border {{ $errors->has('password_confirmation') ? 'border-red-500' : 'border-gray-300' }} rounded-lg focus:ring-blue-500 focus:border-blue-500">
            @error('password_confirmation')
                <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
            @enderror
        </div>
 
        <!-- Submit Button -->
        <button type="submit" class="w-full bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition">
            Register
        </button>
 
        <!-- Redirect to Login -->
        <p class="text-center text-sm text-gray-600 mt-4">
            Already have an account? <a href="/login" class="text-blue-500 hover:underline">Login</a>
        </p>
    </form>
</body>
</html>