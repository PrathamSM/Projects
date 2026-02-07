<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
    <link rel="stylesheet" href="{{ asset('css/app.css') }}">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f9fafa;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #47aed3;
            color: white;
            padding: 1.5rem;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        header h1 {
            margin: 0;
            font-size: 2.5rem;
        }

        header form {
            display: inline-block;
            margin-top: 1rem;
        }

        header button {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            cursor: pointer;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        header button:hover {
            background-color: #d32f2f;
        }

        main {
            padding: 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }

        section {
            margin-bottom: 2rem;
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        section h2 {
            margin-top: 0;
            font-size: 1.8rem;
            color: #333;
            text-align: center;
        }

        .actions {
            text-align: center;
            margin-bottom: 2rem;
        }

        .actions .btn {
            background-color: #008CBA;
            color: white;
            padding: 1rem 2rem;
            margin: 0 0.5rem;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
            font-size: 1rem;
        }

        .actions .btn:hover {
            background-color: #005f73;
        }

        .quiz-results table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1.5rem;
        }

        .quiz-results th, .quiz-results td {
            border: 1px solid #ddd;
            padding: 1rem;
            text-align: center;
            font-size: 1rem;
        }

        .quiz-results th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .quiz-results tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .quiz-results p {
            text-align: center;
            font-size: 1rem;
            color: #666;
        }
    </style>
</head>
<body>
<header>
    <h1>Welcome  {{ auth()->user()->name }}</h1>
    <form method="POST" action="{{ route('logout') }}">
        @csrf
        <button type="submit">Logout</button>
    </form>
</header>
<main>
    <!-- Action Buttons -->
    <section>
        <h2>Dashboard Actions</h2>
        <div class="actions">
            <a href="{{ route('user.courses.index') }}" class="btn">View Courses</a>
            <a href="{{ route('user.courses.enrolled') }}" class="btn">Enrolled Courses</a>
            
        </div>
    </section>

    <!-- Quiz Results -->
    <section>
        <h2>Your Quiz Results</h2>
        @if($results->isEmpty())
            <p>No quiz results available.</p>
        @else
            <div class="quiz-results">
                <table>
                    <thead>
                    <tr>
                        <th>Quiz Title</th>
                        <th>Score</th>
                        <th>Attempted At</th>
                    </tr>
                    </thead>
                    <tbody>
                    @foreach($results as $result)
                        <tr>
                            <td>{{ $result->quiz->title }}</td>
                            <td>{{ $result->score }}%</td>
                            <td>{{ $result->attempted_at }}</td>
                        </tr>
                    @endforeach
                    </tbody>
                </table>
            </div>
        @endif
    </section>
</main>
</body>
</html>
