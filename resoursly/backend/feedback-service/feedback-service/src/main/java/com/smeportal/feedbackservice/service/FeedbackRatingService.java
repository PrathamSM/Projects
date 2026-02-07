package com.smeportal.feedbackservice.service;

import com.smeportal.feedbackservice.dto.UpdateFeedbackReq;
import com.smeportal.feedbackservice.exception.InvalidRatingException;
import com.smeportal.feedbackservice.exception.FeedbackNotFoundException;
import com.smeportal.feedbackservice.model.FeedbackRating;
import com.smeportal.feedbackservice.repository.FeedbackRatingMapper;
import com.smeportal.feedbackservice.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class FeedbackRatingService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackRatingMapper feedbackRatingMapper;

    private static final Logger log = LoggerFactory.getLogger(FeedbackRatingService.class);




    //POST : CREATE FEEDBACK
    public FeedbackRating createFeedback(FeedbackRating feedbackRating) {
        if(feedbackRating.getRating() < 1 || feedbackRating.getRating() > 5) {
            throw new InvalidRatingException("Rating must be between 1 and 5.");
        }

        return feedbackRepository.save(feedbackRating);
    }
    
  //GET AVERAGE RATINGS 
    public Long getAverageRating()
    {
    	return feedbackRepository.getAverageRating() ; 
    }
    

    //GET : Feedback By Profile Id
    public FeedbackRating getFeedbackByProfileId(Long pId) throws FeedbackNotFoundException {
          return feedbackRepository.findByProfileId(pId)
                  .orElseThrow(() -> new FeedbackNotFoundException("Feedback Not Found with Profile ID : " + pId));
    }


    //Update Feedback : Acc to Profile Id
    public FeedbackRating updateFeedback(Long pId, UpdateFeedbackReq updateFeedbackReq) throws FeedbackNotFoundException {

        log.info("Updating feedback for Profile ID: {}", pId);

        FeedbackRating existingFeedback = feedbackRepository.findByProfileId(pId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback Not Found with Profile ID : " + pId));



        log.debug("Before Update: {}", existingFeedback);
        // Update fields manually for debugging
        if (updateFeedbackReq.getGivenBy() != null) {
            existingFeedback.setGivenBy(updateFeedbackReq.getGivenBy());
        }
        if (updateFeedbackReq.getFeedback() != null) {
            existingFeedback.setFeedback(updateFeedbackReq.getFeedback());
        }
        if (updateFeedbackReq.getRating() != null) {
            existingFeedback.setRating(updateFeedbackReq.getRating());
        }

        if(updateFeedbackReq.getPreferences()!=null){
            existingFeedback.setPreferences((updateFeedbackReq.getPreferences()));
        }

        if(updateFeedbackReq.getFeedbacks()!=null){
            existingFeedback.setFeedbacks((updateFeedbackReq.getFeedbacks()));
        }

        if (updateFeedbackReq.getInterviewFeedbacks() != null) {
            existingFeedback.setInterviewFeedbacks(updateFeedbackReq.getInterviewFeedbacks());
        }

        existingFeedback.setFeedbackDate(LocalDateTime.now());
        log.debug("After Update : {}", existingFeedback);

        FeedbackRating updatedFeedback = feedbackRepository.save(existingFeedback);
        log.debug("Updated Feedback: {}", updatedFeedback);
        return updatedFeedback;
    }


    // DELETE: Delete Feedback by Profile ID
    public void deleteFeedback(Long pId) throws FeedbackNotFoundException {
        FeedbackRating existingFeedback = feedbackRepository.findByProfileId(pId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback Not Found with Profile ID: " + pId));
        feedbackRepository.delete(existingFeedback);
        log.info("Deleted feedback for Profile ID: {}", pId);
    }
}
