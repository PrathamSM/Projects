<?php

namespace App\Http\Controllers;

use App\Mail\SendWelcomeMail;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class MailController extends Controller
{
    //
    public function sendEmail(){
       try{
            $toEmailAddress = "smritijian639@gamil.com";
            $welcomeMessage= "Hey welcome to programming Fields .this is mailtrap email configration";
            $response=  Mail::to($toEmailAddress)->send(new SendWelcomeMail($welcomeMessage));
            dd($response);
       } 
       catch(Exception $e){
       // \Log::error("Unable to send mail" . $e->getMessage());
       }
    }
}
