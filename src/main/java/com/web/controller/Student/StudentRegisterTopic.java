package com.web.controller.Student;

import com.web.entity.Student;
import com.web.repository.StudentRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentRegisterTopic {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectRepository subjectRepository;

    @PutMapping("/registerTopic/{subjectId}")
    public ResponseEntity<?> registerTopic(@PathVariable int subjectId){
        return null;
    }
}
