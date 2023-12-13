package com.web.repository;

import com.web.entity.FileComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileComment, Integer> {
    @Query("select f from FileComment f where f.name=:name")
    public FileComment getFileByName(String name);

    @Query("select f from FileComment f")
    public List<FileComment> findAll();
}
