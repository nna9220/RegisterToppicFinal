package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.dto.response.StudentClassResponse;
import com.web.dto.response.SubjectResponse;
import com.web.entity.StudentClass;
import com.web.entity.Subject;
import com.web.mapper.SubjectMapper;
import com.web.repository.PersonRepository;
import com.web.service.Admin.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/admin/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/list")
    public ModelAndView getStClass(){
        List<Subject> subjects = subjectService.getAll();
        List<SubjectResponse> listSub = subjectMapper.toSubjectListDTO(subjects);
        System.out.println("Subject: " + listSub);
        ModelAndView model = new ModelAndView("QuanLyDeTai_Admin");
        model.addObject("listSubject", listSub);
        return model;
    }
    //Duyệt đề tài
    @PutMapping("/browse/{id}")
    public ResponseEntity<?> browseSubjectExisted(@PathVariable int id){
       /* if (CheckedPermission.isAdmin(personRepository)){*/
            return ResponseEntity.ok(subjectService.browseSubject(id));
        /*}else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
    }
}
