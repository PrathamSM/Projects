<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Notifications\Messages\MailMessage;

class EnrollmentSuccessNotification extends Notification implements ShouldQueue
{
    use Queueable;

    public $course;

    /**
     * Create a new notification instance.
     */
    public function __construct($course)
    {
        $this->course = $course;
    }

    /**
     * Get the notification delivery channels.
     */
    public function via($notifiable)
    {
        return ['mail'];
    }

    /**
     * Get the mail representation of the notification.
     */
    public function toMail($notifiable)
    {
        return (new MailMessage)
            ->subject('Enrollment Successful!')
            ->greeting('Hello ' . $notifiable->name . '!')
            ->line('You have successfully enrolled in the course: ' . $this->course->title)
             ->action('View Course', route('user.courses.show', $this->course->id))
            ->line('Thank you for using our platform!');
    }
}
