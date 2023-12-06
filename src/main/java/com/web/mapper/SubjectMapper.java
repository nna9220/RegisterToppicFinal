package com.web.mapper;

import com.web.entity.Subject;
import com.web.dto.request.SubjectRequest;
import com.web.dto.response.SubjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "subject.subjectName", target = "subjectName")
    @Mapping(source = "subject.major", target = "major")
    @Mapping(source = "subject.score", target = "score")
    @Mapping(source = "subject.review", target = "review")
    @Mapping(source = "subject.requirement", target = "requirement")
    @Mapping(source = "subject.expected", target = "expected")
    @Mapping(source = "subject.status", target = "status")
    @Mapping(source = "subject.typeSubject", target = "typeSubject")
    @Mapping(source = "subject.instructorId", target = "instructorId")
    @Mapping(source = "subject.thesisAdvisorId", target = "thesisAdvisorId")
    @Mapping(source = "subject.student1", target = "student1")
    @Mapping(source = "subject.student2", target = "student2")
    @Mapping(source = "subject.tasks", target = "tasks")
    @Mapping(source = "subject.year", target = "year")
    SubjectResponse toResponse(Subject subject);

    List<SubjectResponse> toSubjectListDTO(List<Subject> subjects);

    Subject toEntity(SubjectRequest subjectRequest);
}
