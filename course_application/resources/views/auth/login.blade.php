<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
 
    <form method="POST" action="/login" class="bg-white shadow-lg rounded-lg p-8 max-w-md w-full">
        @csrf
        <h2 class="text-2xl font-bold text-gray-700 mb-6 text-center">Login</h2>
 
     
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
        <div class="mb-6">
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
 
          <!-- Error for Invalid Credentials -->
          @if ($errors->has('login'))
            <div class="mb-4 text-red-500 text-sm text-center">
                {{ $errors->first('login') }}
            </div>
        @endif
 
 
        <!-- Submit Button -->
        <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition">
            Login
        </button>
 
        <!-- Redirect to Register -->
        <p class="text-center text-sm text-gray-600 mt-4">
            Don't have an account? <a href="/register" class="text-blue-500 hover:underline">Register</a>
        </p>
    </form>
</body>
</html>