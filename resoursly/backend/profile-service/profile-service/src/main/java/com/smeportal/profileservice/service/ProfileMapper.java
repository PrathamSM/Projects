package com.smeportal.profileservice.service;
import com.smeportal.profileservice.dto.UpdateProfileReq;
import com.smeportal.profileservice.model.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileFromDto(UpdateProfileReq dto, @MappingTarget Profile entity);
}