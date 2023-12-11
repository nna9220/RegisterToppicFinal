package com.web.controller.Student;

import com.web.config.JwtUtils;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/student/home")
public class HomeStudentController {
    @Autowired
    private UserUtils userUtils;
    @GetMapping
    public ModelAndView getHomeStudent(@RequestParam(name = "token", required = false) String token, HttpServletRequest request) {
        // Xử lý token ở đây, nếu cần
        if (token==null){
            System.out.println("token null");
        }
        HttpSession session = request.getSession();
        Claims claims = JwtUtils.extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27");

        System.out.println(userUtils);
        System.out.println(token);
        System.out.println(session);
        session.setAttribute("token", token);
        System.out.println("Tên " +userUtils.loadUserByUsername(claims.getSubject()).getUsername());

        // Trả về trang HTML với ModelAndView
        ModelAndView modelAndView = new ModelAndView("Dashboard_SinhVien"); // lecturer-home là tên trang HTML
        modelAndView.addObject("token", token);
        modelAndView.addObject("session", session);
        modelAndView.addObject("u", userUtils.loadUserByUsername(claims.getSubject()).getUsername()); // Thêm token vào model nếu cần
        return modelAndView;
    }
}
