package com.smeportal.feedbackservice.repository;

import com.smeportal.feedbackservice.dto.UpdateFeedbackReq;
import com.smeportal.feedbackservice.model.FeedbackRating;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FeedbackRatingMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFeedbackRatingFromDto(UpdateFeedbackReq dto,@MappingTarget FeedbackRating entity);
}
