package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.entity.StudentClass;
import com.web.mapper.StudentClassMapper;
import com.web.dto.request.StudentClassRequest;
import com.web.repository.PersonRepository;
import com.web.service.Admin.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/studentClass")
public class StudentClassController {
    @Autowired
    private StudentClassService studentClassService;
    @Autowired
    private StudentClassMapper studentClassMapper;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/list")
    public ResponseEntity<?> findAllClassExisted(){
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            List<StudentClass> studentClasses = studentClassService.findAll();
            return ResponseEntity.ok(studentClassMapper.toStudentClassListDTO(studentClasses));
        /*}
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }

    @PostMapping("/create")
    public ResponseEntity<?> saveClass(@RequestBody StudentClassRequest studentClass){
        /*if (CheckedPermission.isAdmin(personRepository)) {*/
            studentClassService.createStudentClass(studentClass);
            return ResponseEntity.status(HttpStatus.CREATED).body(studentClass);
        /*
        {
            "classname":"2020-2024"
        }
        */
/*        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }

   @PutMapping("/edit/{classId}")
    public ResponseEntity<?> updateStudentClass(@PathVariable int classId, @RequestBody StudentClassRequest studentClass){
        StudentClass existStudentClass = studentClassService.getStudentClassById(classId);
       /*if (CheckedPermission.isAdmin(personRepository)){*/
           if (existStudentClass != null){
               existStudentClass.setClassname(studentClass.getClassname());
               return new ResponseEntity<>(studentClassService.editStudentClass(existStudentClass),HttpStatus.OK);
           }else {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
       /*}else {
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
       }*/
    }



}
