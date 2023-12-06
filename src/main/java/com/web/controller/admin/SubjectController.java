package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.mapper.SubjectMapper;
import com.web.repository.PersonRepository;
import com.web.service.Admin.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(subjectMapper.toSubjectListDTO(subjectService.getAll()));
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
