package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.config.JwtUtils;
import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/head/home")
public class HomeHeadController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping
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
}
