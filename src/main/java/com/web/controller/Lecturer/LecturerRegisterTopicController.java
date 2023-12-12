package com.web.controller.Lecturer;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.controller.admin.LecturerController;
import com.web.entity.*;
import com.web.mapper.SubjectMapper;
import com.web.dto.request.SubjectRequest;
import com.web.repository.*;
import com.web.service.Admin.StudentService;
import com.web.service.Admin.SubjectService;
import com.web.service.Lecturer.LecturerSubjectService;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer/subject")
public class LecturerRegisterTopicController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private LecturerSubjectService lecturerSubjectService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TypeSubjectRepository typeSubjectRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    private static final Logger logger = LoggerFactory.getLogger(LecturerController.class);
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RegistrationPeriodRepository registrationPeriodRepository;

    @GetMapping
    public ModelAndView getQuanLyDeTai(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            Lecturer existedLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView model = new ModelAndView("QuanLyDeTai_GV");
            List<Subject> subjectByCurrentLecturer = subjectRepository.findSubjectByLecturerIntro(existedLecturer);
            model.addObject("listSubject",subjectByCurrentLecturer);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }


    @PostMapping("/register")
    public ModelAndView lecturerRegisterTopic(@RequestParam("subjectName") String name,
                                              @RequestParam("requirement") String requirement,
                                              @RequestParam("expected") String expected,
                                              @RequestParam(value = "student1", required = false) String student1,
                                              @RequestParam(value = "student2", required = false) String student2,
                                              HttpSession session,
                                              HttpServletRequest request) {

        try {
            LocalDateTime current = LocalDateTime.now();
            System.out.println(current);
            Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
            if (personCurrent.getAuthorities().getName().equals("ROLE_LECTURER") || personCurrent.getAuthorities().getName().equals("ROLE_HEAD") ) {
                Subject newSubject = new Subject();
                newSubject.setSubjectName(name);
                newSubject.setRequirement(requirement);
                newSubject.setExpected(expected);
                newSubject.setStatus(false);
                //Tìm kiếm giảng viên hiện tại
                Lecturer existLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
                newSubject.setInstructorId(existLecturer);
                newSubject.setMajor(existLecturer.getMajor());
                //Tìm sinh viên qua mã sinh viên
                Student studentId1 = studentRepository.findById(student1).orElse(null);
                Student studentId2 = studentRepository.findById(student2).orElse(null);
                if (studentId1!=null){
                    newSubject.setStudentId1(studentId1);
                    newSubject.setStudent1(student1);
                    studentId1.setSubjectId(newSubject);
                }
                if (studentId2!=null){
                    newSubject.setStudentId2(studentId2);
                    newSubject.setStudent2(student2);
                    studentId2.setSubjectId(newSubject);
                }
                LocalDate nowDate = LocalDate.now();
                newSubject.setYear(String.valueOf(nowDate));
                TypeSubject typeSubject = typeSubjectRepository.findById(1).orElse(null);
                newSubject.setTypeSubject(typeSubject);
                subjectRepository.save(newSubject);
                studentRepository.save(studentId1);
                studentRepository.save(studentId2);
                String referer = "http://localhost:5000/api/lecturer/subject";
                // Thực hiện redirect trở lại trang trước đó
                System.out.println("Url: " + referer);
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);
            }
            else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Bạn không có quyền truy cập.");
                return error;
            }
        }catch (Exception e){
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "lỗi.");
            return error;
        }
    }

    /*@GetMapping("/{id}")
    public ModelAndView getEditPage(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_LECTURER") || personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            List<RegistrationPeriod> registrationPeriods = registrationPeriodRepository.findAllPeriod();
            LocalDateTime current = LocalDateTime.now();
            System.out.println(current);
            for (RegistrationPeriod registrationPeriod:registrationPeriods) {

            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }*/

    //@PostMapping("/edit/{id}")
}
