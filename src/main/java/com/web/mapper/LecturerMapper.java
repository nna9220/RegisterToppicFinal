package com.web.mapper;

import com.web.entity.Lecturer;
import com.web.dto.request.LecturerRequest;
import com.web.dto.response.LecturerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper {

    LecturerResponse toResponse(Lecturer lecturer);

    List<LecturerResponse> toLecturerListDTO(List<Lecturer> lecturers);

    Lecturer toEntity(LecturerRequest lecturerRequest);
}
