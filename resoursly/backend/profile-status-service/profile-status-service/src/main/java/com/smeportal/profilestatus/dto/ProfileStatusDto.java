package com.smeportal.profilestatus.dto;

import com.smeportal.profilestatus.converter.JsonConverter;
import com.smeportal.profilestatus.model.Availability;
import jakarta.persistence.Convert;
import lombok.Data;

@Data
public class ProfileStatusDto {


        private Boolean ndaSigned;
        @Convert(converter = JsonConverter.class)
        private Availability availability;
        private Boolean isApproved;


}
