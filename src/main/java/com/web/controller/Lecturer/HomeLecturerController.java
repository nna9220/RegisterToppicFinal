package com.web.controller.Lecturer;

import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.dto.request.PersonRequest;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.entity.Subject;
import com.web.exception.NotFoundException;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer")
public class HomeLecturerController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonService personService;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @GetMapping("/home")
    public ModelAndView getHome(HttpSession session,@RequestParam(name = "token", required = false) String token, HttpServletRequest request) {
        // Xử lý token ở đây, nếu cần
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            // Trả về trang HTML với ModelAndView
            ModelAndView modelAndView = new ModelAndView("Dashboard_GiangVien"); // lecturer-home là tên trang HTML
            modelAndView.addObject("token", token);
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("session", session);// Thêm token vào model nếu cần
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            Lecturer currentLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileGV");
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("lec", currentLecturer);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }

    @GetMapping("/counterArgumentSubject")
    public ModelAndView getCounterArgument(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            Lecturer currentLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            List<Subject> listSubject = subjectRepository.findSubjectsByThesisAdvisorId(currentLecturer);
            ModelAndView modelAndView = new ModelAndView("lecturer_listReviewTopic");
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("lec", currentLecturer);
            modelAndView.addObject("listSubject",listSubject);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }

    @GetMapping("/edit")
    public ModelAndView getEditProfile(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            Lecturer lecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileGV");
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateLecturer(@PathVariable String id,@ModelAttribute PersonRequest studentRequest,
                                      HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            Lecturer existLecturer = lecturerRepository.findById(id).orElse(null);
            if (existLecturer!=null){
                System.out.println(id);
                existLecturer.getPerson().setFirstName(studentRequest.getFirstName());
                existLecturer.getPerson().setLastName(studentRequest.getLastName());

                existLecturer.getPerson().setBirthDay(String.valueOf(studentRequest.getBirthDay()));
                existLecturer.getPerson().setPhone(studentRequest.getPhone());
                existLecturer.getPerson().setStatus(studentRequest.isStatus());

                lecturerRepository.save(existLecturer);
                String referer = Contains.URL_LOCAL + "/api/lecturer/profile";
                System.out.println("Url: " + referer);
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);

            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tìm thấy sinh viên");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
