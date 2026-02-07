package com.smeportal.feedbackservice.controller;

import com.smeportal.feedbackservice.dto.UpdateFeedbackReq;
import com.smeportal.feedbackservice.exception.FeedbackNotFoundException;
import com.smeportal.feedbackservice.model.FeedbackRating;
import com.smeportal.feedbackservice.service.FeedbackRatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/feedbacks")
@RestController
@CrossOrigin("http://localhost:5173")
public class FeedbackRatingController {

    @Autowired
    private FeedbackRatingService feedbackRatingService;

    //CREATE FEEDBACK
    @PostMapping
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackRating feedbackRating) {
            FeedbackRating createdFeedback = feedbackRatingService.createFeedback(feedbackRating);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
    }


    //GET FEEDBACK BY PROFILE ID
    @GetMapping("/{pId}")
    public ResponseEntity<?> getFeedbackByProfileId(@PathVariable Long pId) throws FeedbackNotFoundException {
        FeedbackRating feedbackRating = feedbackRatingService.getFeedbackByProfileId(pId);
        return ResponseEntity.status(HttpStatus.OK).body(feedbackRating);
    }


    //Update the Feedback Acc to Profile ID
    @PutMapping("/{pId}")
    public ResponseEntity<?> updateFeedbackByProfileId(@PathVariable Long pId, @RequestBody @Valid UpdateFeedbackReq updateFeedbackReq) throws FeedbackNotFoundException {
        FeedbackRating updatedFeedback = feedbackRatingService.updateFeedback(pId, updateFeedbackReq);
//        System.out.println("UPDATED FEEDBACK : " + updatedFeedback );
        Map<String, String> successMsg = new HashMap<>();
        successMsg.put("success", "Feedback Updated Successfully!");

        return ResponseEntity.status(HttpStatus.OK).body(successMsg);

    }


    // DELETE: Delete Feedback by Profile ID
    @DeleteMapping("/{pId}")
    public ResponseEntity<?> deleteFeedbackByProfileId(@PathVariable Long pId) throws FeedbackNotFoundException {
        feedbackRatingService.deleteFeedback(pId);
        Map<String, String> successMsg = new HashMap<>();
        successMsg.put("success", "Feedback deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(successMsg);
    }
    
    

    //Get Average Ratings
    @GetMapping("/averageRating")
    public ResponseEntity<?> getAverageRating()
    {
    	Long averageRatingCount = feedbackRatingService.getAverageRating();
        return ResponseEntity.status(HttpStatus.OK).body("averageRating: "+averageRatingCount);

    }



}
