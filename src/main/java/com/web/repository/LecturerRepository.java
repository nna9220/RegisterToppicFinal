package com.web.repository;

import com.web.entity.Lecturer;
import com.web.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {
    @Query("SELECT l FROM Lecturer l")
    List<Lecturer> findAllLec();

    @Query("SELECT p FROM Lecturer p WHERE p.lecturerId=:id")
    public Lecturer getUserByEmail(@Param("id") String id);

}
