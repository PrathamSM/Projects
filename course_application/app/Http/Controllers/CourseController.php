<?php
 
namespace App\Http\Controllers;
 
use App\Http\Controllers\Controller;
use App\Models\Course;
use App\Models\Enrollment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
 
class CourseController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
 
        $search = $request->input('search');
 
    $courses = Course::query()
        ->when($search, function ($query, $search) {
            return $query->where('title', 'like', "%{$search}%")
                         ->orWhere('instructor_name', 'like', "%{$search}%");
        })
        ->paginate(6);
 
    return view('course.index', compact('courses'));
    }
 
   static public function userIndex()
{
    $courses = Course::all();
    return view('user.index', compact('courses'));
}
 
    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        // dd('Create method called');
        return view('course.create');
    }
 
    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validatedData = $request->validate([
            'title' => 'required|string|max:255',
            'description' => 'required|string',
            'instructor_name' => 'required|string|max:255',
            'pdf_files' => 'nullable|array',
            'pdf_files.*' => 'file|mimes:pdf|max:20480', // Allow PDF files up to 20MB
            'video_links' => 'nullable|array',
            'video_links.*' => 'nullable|url', // Validate URLs
        ]);
   
        $contentFiles = [];
   
        // Handle PDF uploads
        if ($request->hasFile('pdf_files')) {
            foreach ($request->file('pdf_files') as $file) {
                $originalName = $file->getClientOriginalName();
                $path = $file->storeAs('pdf_files', $originalName, 'public');
                $contentFiles[] = ['type' => 'pdf', 'path' => $path];
            }
        }
   
        // Handle video links
        if ($request->has('video_links')) {
            foreach ($request->input('video_links') as $link) {
               if(!empty($link)) {
                $contentFiles[] = ['type' => 'video', 'link' => $link];
               }
            }
        }
   
        $course = Course::create([
            'title' => $validatedData['title'],
            'description' => $validatedData['description'],
            'instructor_name' => $validatedData['instructor_name'],
            'content_files' => $contentFiles
        ]);
   
        return redirect()->back()->with('success', 'Course created successfully');
    }
 
    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $course = Course::findOrFail($id);
        return view('course.show', compact('course'));
    }
 
 
    public function userShow($id)
    {
       
 
        $course = Course::findOrFail($id);
        $isEnrolled = Enrollment::where('user_id', Auth::id())->where('course_id', $id)->exists();
        return view('course.user_show', compact('course', 'isEnrolled'));
    }
 
    /**
     * Show the form for editing the specified resource.
     */
    public function edit($id)
    {
        $course = Course::findOrFail($id);
        return view('course.edit', compact('course'));
    }
 
    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
{
    $course = Course::findOrFail($id);
 
    // Validate the request
    $request->validate([
        'title' => 'required|string|max:255',
        'description' => 'required|string',
        'instructor_name' => 'required|string|max:255',
        'pdf_files.*' => 'file|mimes:pdf|max:20480', // Allow PDF files up to 20MB
        'video_links' => 'nullable|string',
    ]);
 
 
     // Custom validation for video links
     $videoLinks = $request->input('video_links', '');
     $videoLinksArray = array_filter(array_map('trim', explode("\n", $videoLinks)));
 
     foreach ($videoLinksArray as $link) {
         if (!filter_var($link, FILTER_VALIDATE_URL)) {
             return redirect()->back()->withErrors(['video_links' => 'Each video link must be a valid URL'])->withInput();
         }
     }
 
    // Update course details
    $course->title = $request->input('title');
    $course->description = $request->input('description');
    $course->instructor_name = $request->input('instructor_name');
 
    // Get the current content files
    $contentFiles = $course->content_files ?? [];
 
    // Handle existing PDFs removal
    if ($request->has('remove_pdfs')) {
        $contentFiles = array_filter($contentFiles, function ($file) use ($request) {
            return !isset($file['path']) || !in_array($file['path'], $request->input('remove_pdfs'));           //Checks if the files path is the amongst the path from the 'remove_pdf'
        });
    }
 
    // Handle new PDFs upload
    if ($request->hasFile('pdf_files')) {
        foreach ($request->file('pdf_files') as $file) {
            $originalName = $file->getClientOriginalName();
            $path = $file->storeAs('pdf_files', $originalName, 'public');
            $contentFiles[] = ['type' => 'pdf', 'path' => $path];
        }
    }
 
    // Handle existing video links removal
    if ($request->has('remove_videos')) {
        $contentFiles = array_filter($contentFiles, function ($file) use ($request) {
            return !isset($file['link']) || !in_array($file['link'], $request->input('remove_videos'));
        });
    }
 
 
 
    foreach ($videoLinksArray as $link) {
        $contentFiles[] = ['type' => 'video', 'link' => $link];
    }
 
   
 
    // Update the content_files attribute
    $course->content_files = $contentFiles;
    $course->save();
 
    return redirect()->route('courses.index')->with('success', 'Course updated successfully.');
}
 
    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $course = Course::findOrFail($id);
        $course->delete();
 
        return redirect()->route('courses.index')->with('success', 'Course Deleted Successfully');
    }
 
    //06/12
    public function enrolledCourses()
    {
        $userId = Auth::id();
        $enrolledCourseIds = Enrollment::where('user_id', $userId)->pluck('course_id');
        $courses = Course::whereIn('id', $enrolledCourseIds)->paginate(6);
 
        return view('user.enrolled', compact('courses'));
    }
 
    //06/12
    public function notEnrolledCourses(Request $request) {
        $userId = Auth::id();
        $enrolledCourseIds = Enrollment::where('user_id', $userId)->pluck('course_id');
 
        $search = $request->input('search');
       
        $courses = Course::query()
                    ->whereNotIn('id', $enrolledCourseIds)
                    ->when($search, function ($query, $search) {
                        return $query->where('title', 'like', "%{$search}%")
                        ->orWhere('instructor_name', 'like', "%{$search}%");
                    })->paginate(6);
 
 
        return view('user.index', compact('courses'));
    }
 
 
   
}