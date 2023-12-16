package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.dto.request.PersonRequest;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/head")
public class HomeHeadController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    @GetMapping("/home")
    public ModelAndView getHome(HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            // Trả về trang HTML với ModelAndView
            ModelAndView modelAndView = new ModelAndView("Dashboard_TBM"); // lecturer-home là tên trang HTML
            modelAndView.addObject("person", personCurrent);
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
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Lecturer currentLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileTBM");
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("lec", currentLecturer);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }
    @GetMapping("/edit")
    public ModelAndView getEditProfile(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Person person = personRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileTBM");
            modelAndView.addObject("person", person);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateLecturer(@PathVariable String id,@ModelAttribute PersonRequest studentRequest,
                                       HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Person existPerson = personRepository.findById(id).orElse(null);
            if (existPerson!=null){
                System.out.println(id);
                existPerson.setFirstName(studentRequest.getFirstName());
                existPerson.setLastName(studentRequest.getLastName());
                existPerson.setBirthDay(String.valueOf(studentRequest.getBirthDay()));
                existPerson.setPhone(studentRequest.getPhone());
                existPerson.setStatus(studentRequest.isStatus());

                personRepository.save(existPerson);
                String referer = Contains.URL + "/api/head/profile";
                System.out.println("Url: " + referer);
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);

            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tìm thấy admin");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
