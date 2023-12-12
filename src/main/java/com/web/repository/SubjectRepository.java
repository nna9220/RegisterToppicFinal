package com.web.repository;

import com.web.entity.Lecturer;
import com.web.entity.Major;
import com.web.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Query("select s from Subject s")
    public List<Subject> findAllSubject();

    @Query("select s from Subject s where s.instructorId=:id")
    public List<Subject> findSubjectByLecturerIntro(Lecturer id);

    @Query("select s from Subject s where s.status=:status and s.major=:major")
    public List<Subject> findSubjectByStatusAndMajor(boolean status, Major major);


}
