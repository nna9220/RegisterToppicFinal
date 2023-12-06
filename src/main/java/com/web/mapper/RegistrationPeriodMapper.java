package com.web.mapper;

import com.web.entity.RegistrationPeriod;
import com.web.dto.request.RegistrationPeriodRequest;
import com.web.dto.response.RegistrationPeriodResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegistrationPeriodMapper {
    @Mapping(source = "registrationPeriod.periodId", target = "periodId")
    @Mapping(source = "registrationPeriod.registrationTimeStart", target = "registrationTimeStart")
    @Mapping(source = "registrationPeriod.registrationTimeEnd", target = "registrationTimeEnd")
    @Mapping(source = "registrationPeriod.registrationName", target = "registrationName")
    @Mapping(source = "registrationPeriod.typeSubjectId", target = "typeSubjectId")
    RegistrationPeriodResponse toResponse(RegistrationPeriod registrationPeriod);

    List<RegistrationPeriodResponse> toRegistrationPeriodListDTO(List<RegistrationPeriod> registrationPeriods);

    RegistrationPeriod toEntity(RegistrationPeriodRequest registrationPeriodRequest);
}
