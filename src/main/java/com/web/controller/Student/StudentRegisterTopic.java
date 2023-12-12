package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.entity.Subject;
import com.web.repository.PersonRepository;
import com.web.repository.StudentRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.StudentService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student/subject")
public class StudentRegisterTopic {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public ModelAndView getListSubject(HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Optional<Student> currentStudentOptional = studentRepository.findById(personCurrent.getPersonId());
            if (currentStudentOptional.isPresent()) {
                Student currentStudent = currentStudentOptional.get();
                if (currentStudent.getSubjectId() == null) {
                    ModelAndView modelAndView = new ModelAndView("QuanLyDeTai_SV");
                    List<Subject> subjectList = subjectRepository.findSubjectByStatusAndMajorAndStudent(true, currentStudent.getMajor());
                    modelAndView.addObject("subjectList", subjectList);
                    return modelAndView;
                } else {
                    ModelAndView modelAndView = new ModelAndView("QuanLyDeTaiDaDK_SV");
                    Subject existSubject = subjectRepository.findById(currentStudent.getSubjectId().getSubjectId()).orElse(null);
                    modelAndView.addObject("subject", existSubject);
                    return modelAndView;
                }
            } else {
                return new ModelAndView("error").addObject("errorMessage", "Không tìm thấy sinh viên.");
            }
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }

    @PostMapping("/registerTopic/{subjectId}")
    public ModelAndView registerTopic(@PathVariable int subjectId, HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            Student currentStudent = studentRepository.findById(personCurrent.getPersonId()).orElse(null);
            Subject existSubject = subjectRepository.findById(subjectId).orElse(null);
            if (existSubject!=null){
                if (existSubject.getStudent1() == null) {
                    existSubject.setStudent1(currentStudent.getStudentId());
                    existSubject.setStudentId1(currentStudent);
                    currentStudent.setSubjectId(existSubject);
                } else if (existSubject.getStudent2() == null) {
                    existSubject.setStudent2(currentStudent.getStudentId());
                    existSubject.setStudentId2(currentStudent);
                    currentStudent.setSubjectId(existSubject);
                } else {
                    ModelAndView error = new ModelAndView();
                    error.addObject("errorMessage", "Đã đủ số lượng SVTH");
                    return error;
                }
                subjectRepository.save(existSubject);
                studentRepository.save(currentStudent);
                String referer = request.getHeader("Referer");
                return new ModelAndView("redirect:" + referer);
            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tồn tại đề tài này.");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
