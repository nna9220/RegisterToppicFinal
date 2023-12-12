package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Subject;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.SubjectService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/head/subject")
public class AddCounterArgumentController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private UserUtils userUtils;
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

    @GetMapping
    public ModelAndView getDanhSachDeTai(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Lecturer existedLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView model = new ModelAndView("Duyet_TBM");
            List<Subject> subjectByCurrentLecturer = subjectRepository.findSubjectByStatusAndMajor(false,existedLecturer.getMajor());
            model.addObject("listSubject",subjectByCurrentLecturer);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/browse/{id}")
    public ModelAndView browseSubject(@PathVariable int id, HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD") ) {
            subjectService.browseSubject(id);
            String referer = "http://localhost:8080/api/head/subject";
            // Thực hiện redirect trở lại trang trước đó
            System.out.println("Url: " + referer);
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
