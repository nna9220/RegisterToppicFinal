package com.web.controller.admin;

import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.dto.request.PersonRequest;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import com.web.config.JwtUtils;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class HomeAdminController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/home")
    public ModelAndView getHome(HttpSession session, HttpServletRequest request) {

        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            // Trả về trang HTML với ModelAndView
            ModelAndView modelAndView = new ModelAndView("Dashboard_Admin");
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
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Person person = personRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileAdmin");
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        } else {
            return new ModelAndView("error").addObject("errorMessage", "Bạn không có quyền truy cập.");
        }
    }
    @GetMapping("/edit")
    public ModelAndView getEditProfile(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent != null && personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Person person = personRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView modelAndView = new ModelAndView("profileAdmin");
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
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Person existPerson = personRepository.findById(id).orElse(null);
            if (existPerson!=null){
                System.out.println(id);
                existPerson.setFirstName(studentRequest.getFirstName());
                existPerson.setLastName(studentRequest.getLastName());
                existPerson.setBirthDay(String.valueOf(studentRequest.getBirthDay()));
                existPerson.setPhone(studentRequest.getPhone());
                existPerson.setStatus(studentRequest.isStatus());

                personRepository.save(existPerson);
                String referer = Contains.URL + "/api/admin/profile";
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
