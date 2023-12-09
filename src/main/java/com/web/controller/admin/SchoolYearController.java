package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.JwtUtils;
import com.web.dto.request.StudentClassRequest;
import com.web.entity.Person;
import com.web.entity.SchoolYear;
import com.web.entity.StudentClass;
import com.web.mapper.SchoolYearMapper;
import com.web.dto.request.SchoolYearRequest;
import com.web.repository.PersonRepository;
import com.web.repository.SchoolYearRepository;
import com.web.service.Admin.SchoolYearService;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/admin/schoolYear")
public class SchoolYearController {
    @Autowired
    private SchoolYearService schoolYearService;
    @Autowired
    private SchoolYearMapper schoolYearMapper;
    @Autowired
    private SchoolYearRepository schoolYearRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserUtils userUtils;

    @GetMapping
    public ModelAndView getAllSchoolYear(HttpSession session){
        String token = (String) session.getAttribute("token");
        Claims claims = JwtUtils.extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27");
        UserDetails email = userUtils.loadUserByUsername(claims.getSubject());
        Person person = personRepository.findUserByEmail(email.getUsername());
        if (person.getAuthorities().getName().equals("ROLE_ADMIN")){
            List<SchoolYear> schoolYears = schoolYearService.findAll();
            ModelAndView model = new ModelAndView("admin_addYear");
            model.addObject("listYear", schoolYears);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public ModelAndView saveSchoolYear(@RequestParam("year") String schoolYearRequest, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        SchoolYearRequest schoolYearRequest1 = new SchoolYearRequest();
        schoolYearRequest1.setYear(schoolYearRequest);
        schoolYearService.createSchoolYear(schoolYearRequest1);
        return new ModelAndView("redirect:" + referer);
    }

    @GetMapping("/{yearId}")
    public ModelAndView editClass(@PathVariable int yearId) {
        // Lấy thông tin lớp học cần chỉnh sửa từ service
        SchoolYear schoolYear = schoolYearRepository.findById(yearId).orElse(null);

        // Kiểm tra xem lớp học có tồn tại không
        if (schoolYear != null) {
            // Trả về ModelAndView với thông tin lớp học và đường dẫn của trang chỉnh sửa
            ModelAndView model = new ModelAndView("admin_editYear");
            model.addObject("schoolYear", schoolYear);
            return model;
        } else {
            // Trả về ModelAndView với thông báo lỗi nếu không tìm thấy lớp học
            ModelAndView errorModel = new ModelAndView("error");
            errorModel.addObject("errorMessage", "Không tìm thấy lớp học");
            return errorModel;
        }
    }


    @PostMapping("/edit/{yearId}")
    public ModelAndView updateYear(@PathVariable int yearId, @ModelAttribute SchoolYearRequest schoolYearRequest, @ModelAttribute("successMessage") String successMessage){
        SchoolYear existSchoolYear = schoolYearRepository.findById(yearId).orElse(null);

        if (existSchoolYear != null){
            existSchoolYear.setYear(schoolYearRequest.getYear());
            schoolYearRepository.save(existSchoolYear);
            String url = "http://localhost:8080/api/admin/schoolYear";
            ModelAndView model = new ModelAndView("redirect:" + url);

            model.addObject("successMessage", successMessage);
            return model;
        }else {
            return new ModelAndView("redirect:");
        }
    }

}
