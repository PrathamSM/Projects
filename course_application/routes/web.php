<?php
 
use App\Http\Controllers\AdminController;
use App\Models\User;
 use App\Models\Course;
 
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\CourseController;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\EnrollmentController;
use App\Http\Controllers\MailController;
use App\Http\Controllers\QuestionController;
use App\Http\Controllers\QuizController;
use App\Http\Controllers\ResultController;
use App\Http\Middleware\AdminMiddleware;
use App\Http\Middleware\UserMiddleware;
use App\Models\quiz;
use LDAP\Result;
 
 
 
//Route::get('/courses/{course}/quiz', [QuizController::class, 'attemptQuiz'])->name('quizzes.attempt');
 
 
 
 
 
 
Route::get('/', function() {
    return redirect()->route('login');
});
 
// Guest Routes
Route::middleware(['guest'])->group(function () {
    Route::view('/login', 'auth.login')->name('login'); // Login view
    Route::view('/register', 'auth.register')->name('register'); // Registration view
    Route::post('/login', [AuthController::class, 'login']); // Handle login
    Route::post('/register', [AuthController::class, 'register']); // Handle registration
});
 
// User-Specific Routes
Route::middleware(['auth', UserMiddleware::class])->group(function () {
 
    //Route::view('/dashboard', 'dashboard.index')->name('dashboard');
    Route::get('/dashboard', [ResultController::class, 'index'])->name('dashboard.index');
 
 
    Route::get('/user/courses', [CourseController::class, 'notEnrolledCourses'])->name('user.courses.index');
    Route::get('/user/course/{id}', [CourseController::class, 'userShow'])->name('user.courses.show');
 
    //enroll
    Route::post('/user/course/{id}/enroll', [EnrollmentController::class, 'enroll'])->name('user.courses.enroll');
   
        //06/12
        Route::get('/user/courses/enrolled', [CourseController::class, 'enrolledCourses'])->name('user.courses.enrolled');
        //from this url user can access the attempt quiz
        Route::get('/courses/{course}/quiz', [QuizController::class, 'attemptQuiz'])->name('quizzes.attempt');
        Route::post('quizzes/{quiz}/result', [ResultController::class, 'store'])->name('quizzes.result.store');
        Route::post('quizzes/{quiz}/submit', [ResultController::class, 'store'])->name('results.store');
});
 
// Admin-Specific Routes
Route::middleware(['auth', AdminMiddleware::class])->group(function () {
 
    // Route::view('/admin', 'admin.dashboard')->name('admin.dashboard'); // Admin dashboard
    Route::get('/admin', function() {
        $courseCount = AdminController::getCourseCount();
        $enrollmentCount = AdminController::getTotalEnrollments();
        $quizzes = quiz::with('results:id,quiz_id,score') // Eager load only necessary fields
    ->get()
    ->map(function ($quiz) {
        $averageScore = $quiz->results->avg('score');
        return [
            'title' => $quiz->title,
            'averageScore' => $averageScore ? round($averageScore, 2) : 0,
        ];
    });
 
        return view('admin.dashboard', compact('courseCount', 'enrollmentCount', 'quizzes'));
    })->name('admin.dashboard');
    Route::get('/admin/courses/create', [CourseController::class, 'create'])->name('courses.create');
 
    Route::get('/admin/courses', [CourseController::class, 'index'])->name('courses.index');
    Route::get('/admin/courses/{id}/edit', [CourseController::class, 'edit'])->name('courses.edit');
    // Route::get('/admin/courses/{id}', [CourseController::class, 'destroy'])->name('courses.destroy');
    Route::post('/admin/courses', [CourseController::class, 'store'])->name('courses.store');
 
    Route::put('/admin/courses/{id}', [CourseController::class, 'update'])->name('courses.update');
    Route::delete('/admin/courses/{id}', [CourseController::class, 'destroy'])->name('courses.destroy');
 
 
 
    Route::get('/admin/courses/{id}', [CourseController::class, 'show'])->name('courses.show');
    //to create quiz
    Route::get('/quizzes/create', [QuizController::class, 'create'])->name('quizzes.create');
    //to create questions
    Route::get('quizzes/{quiz}/questions/create', [QuestionController::class, 'create'])->name('questions.create');
    Route::post('/quizzes', [QuizController::class, 'store'])->name('quizzes.store');
    Route::get('/quizzes/{quiz}', [QuizController::class, 'show'])->name('quizzes.show');
    Route::post('quizzes/{quiz}/questions', [QuestionController::class, 'store'])->name('questions.store');
 
    Route::get('/storage/pdf_files/{filename}', function ($filename) {
        $path = storage_path('app/public/pdf_files/' . $filename);
        if (!file_exists($path)) {
            abort(404);
        }
        return response()->file($path);
    });
});
 
Route::middleware('auth')->post('/logout', [AuthController::class, 'logout'])->name('logout');
 
Route::get('/test-enroll', function () {
    $user = User::first(); // Assuming a user exists in the database
    $course = Course::first(); // Assuming a course exists in the database
   
    $user->notify(new \App\Notifications\EnrollmentSuccessNotification($course));
   
    return 'Enrollment email sent!';
});
 
Route::get('logs', [\Rap2hpoutre\LaravelLogViewer\LogViewerController::class, 'index']);