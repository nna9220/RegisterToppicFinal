package com.web.controller.admin;

import com.web.config.JwtUtils;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin/detail")
public class DetailAdminController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public ModelAndView getDetail(HttpServletRequest request){
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        System.out.println("token"+token);
        Claims claims = JwtUtils.extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27");
        UserDetails user = userUtils.loadUserByUsername(claims.getSubject());
        String email = user.getUsername();
        System.out.println("email: "+email);
        Person person = personRepository.findUserByEmail(email);
        ModelAndView model = new ModelAndView("profileAdmin");
        model.addObject("person",person);
        return model;
    }
}
