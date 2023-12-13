package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.entity.Subject;
import com.web.repository.PersonRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/student/home")
public class HomeStudentController {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PersonRepository personRepository;
    @GetMapping
    public ModelAndView getListSubject(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_STUDENT")) {
            ModelAndView modelAndView = new ModelAndView("Dashboard_SinhVien");
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

}
