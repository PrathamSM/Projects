package com.smeportal.feedbackservice.repository;

import com.smeportal.feedbackservice.model.FeedbackRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackRating, Long> {

    public Optional<FeedbackRating> findByProfileId(Long pId);
    
    @Query(value = "SELECT AVG(rating) FROM feedback_rating WHERE rating IS NOT NULL", nativeQuery = true)
    public  Long getAverageRating();
}
