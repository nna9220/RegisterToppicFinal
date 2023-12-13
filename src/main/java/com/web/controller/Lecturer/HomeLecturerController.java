package com.web.controller.Lecturer;

import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.entity.Person;
import com.web.exception.NotFoundException;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer/home")
public class HomeLecturerController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;
    @GetMapping
    public ModelAndView getHome(HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_LECTURER")) {
            // Trả về trang HTML với ModelAndView
            ModelAndView modelAndView = new ModelAndView("Dashboard_GiangVien");
            modelAndView.addObject("person", personCurrent);
        return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
