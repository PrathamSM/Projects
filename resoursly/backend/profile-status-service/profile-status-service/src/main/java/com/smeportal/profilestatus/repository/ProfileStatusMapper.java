package com.smeportal.profilestatus.repository;


import com.smeportal.profilestatus.dto.ProfileStatusDto;
import com.smeportal.profilestatus.model.ProfileStatus;
import org.mapstruct.*;


@Mapper(componentModel = "spring")

public interface ProfileStatusMapper {
    /**
     * Updates the target ProfileStatus entity with non-null properties from the source.
     *
     * @param source the incoming ProfileStatus containing the updates
     * @param target the existing ProfileStatus entity to be updated
     */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileStatusFromDto(ProfileStatusDto source, @MappingTarget ProfileStatus target);
}
