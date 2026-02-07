package com.smeportal.searchms.client;

import com.smeportal.searchms.model.FeedbackRating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feedback-service", url = "${feedback-service.url}")
public interface FeedbackRatingServiceClient {

    @GetMapping("/{pId}")
    FeedbackRating getFeedbackByProfileId(@PathVariable Long pId);
}
