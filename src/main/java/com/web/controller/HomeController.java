package com.web.controller;

import com.web.dto.response.LecturerResponse;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.mapper.LecturerMapper;
import com.web.service.Admin.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private LecturerMapper lecturerMapper;
    @GetMapping("/contact")
    public ModelAndView getContact(){
        ModelAndView model = new ModelAndView("contact");
        return model;
    }
    @GetMapping("/team")
    public ModelAndView getTeam(){
        List<Lecturer> lecturers = lecturerService.getAllLec();
        List<LecturerResponse> listLec = lecturerMapper.toLecturerListDTO(lecturers);
        for (LecturerResponse p:listLec) {
            System.out.println(p.getPerson());
        }
        System.out.println("GiangVien: " + listLec);
        ModelAndView model = new ModelAndView("team");
        model.addObject("listLecturer", listLec);
        return model;
    }

    @GetMapping("/intruction")
    public ModelAndView getIntruction(){
        ModelAndView model = new ModelAndView("intruction");
        return model;
    }
}
