package com.web.controller.HeadOfDepartment;

import com.web.config.JwtUtils;
import com.web.entity.Person;
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
    private PersonService personService;
    @GetMapping
    public ModelAndView getHome(@RequestParam(name = "token", required = false) String token, HttpServletRequest request) {
        // Xử lý token ở đây, nếu cần
        if (token==null){
            System.out.println("token null");
        }
        HttpSession session = request.getSession();
        Claims claims = JwtUtils.extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27");
        System.out.println(userUtils);
        System.out.println(token);
        System.out.println(session);
        String email = userUtils.loadUserByUsername(claims.getSubject()).getUsername();
        Person currentUser = personService.getUserEmail(email);
        if (currentUser.getAuthorities().getName().equals("ROLE_HEAD")) {
            // Trả về trang HTML với ModelAndView
            ModelAndView modelAndView = new ModelAndView("Dashboard_TBM"); // lecturer-home là tên trang HTML
            modelAndView.addObject("token", token);
            modelAndView.addObject("session", session);
            modelAndView.addObject("u", email); // Thêm token vào model nếu cần
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
