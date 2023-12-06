package com.web.controller.HeadOfDepartment;

import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Subject;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/headOfDepartment")
public class AddCounterArgumentController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @PutMapping("/addCounterArgumrnt/{subjectId}")
    public ResponseEntity<?> addCounterArgumrnt(@PathVariable int subjectId, @RequestParam("LecturerId") String lecturerId){
        Subject existedSubject = subjectRepository.findById(subjectId).orElse(null);
        if(existedSubject!=null){
            Lecturer currentLecturer = lecturerRepository.findById(lecturerId).orElse(null);
            List<Subject> addSub = new ArrayList<>();
            addSub.add(existedSubject);
            if (currentLecturer!=null) {
                currentLecturer.setListSubCounterArgument(addSub);
                existedSubject.setThesisAdvisorId(currentLecturer);
                lecturerRepository.save(currentLecturer);
                subjectRepository.save(existedSubject);
            }
            System.out.println(currentLecturer.getListSubCounterArgument());
            return ResponseEntity.ok(existedSubject);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
