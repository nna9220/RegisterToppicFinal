package com.web.mapper;

import com.web.entity.StudentClass;
import com.web.dto.request.StudentClassRequest;
import com.web.dto.response.StudentClassResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentClassMapper {

    StudentClassResponse toResponse(StudentClass studentClass);

    List<StudentClassResponse> toStudentClassListDTO(List<StudentClass> studentClasses);

    StudentClass toEntity(StudentClassRequest studentClassRequest);
}
