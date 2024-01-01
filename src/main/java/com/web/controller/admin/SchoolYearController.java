package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.CheckRole;
import com.web.dto.response.LecturerResponse;
import com.web.dto.response.SchoolYearResponse;
import com.web.entity.*;
import com.web.config.JwtUtils;
import com.web.dto.request.StudentClassRequest;
import com.web.mapper.SchoolYearMapper;
import com.web.dto.request.SchoolYearRequest;
import com.web.repository.PersonRepository;
import com.web.repository.SchoolYearRepository;
import com.web.service.Admin.SchoolYearService;
import com.web.utils.Contains;
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
    private SchoolYearRepository schoolYearRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserUtils userUtils;


    @GetMapping
    public ModelAndView getAllSubject(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            List<SchoolYear> schoolYears = schoolYearService.findAll();
            ModelAndView modelAndView = new ModelAndView("QuanLyNienKhoa");
            modelAndView.addObject("person", personCurrent);
            modelAndView.addObject("listYear",schoolYears);
            return modelAndView;
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
    public ModelAndView editClass(HttpSession session, @PathVariable int yearId) {
        // Lấy thông tin lớp học cần chỉnh sửa từ service
        SchoolYear schoolYear = schoolYearRepository.findById(yearId).orElse(null);
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        // Kiểm tra xem lớp học có tồn tại không
        if (schoolYear != null) {
            // Trả về ModelAndView với thông tin lớp học và đường dẫn của trang chỉnh sửa
            ModelAndView model = new ModelAndView("admin_editYear");
            model.addObject("schoolYear", schoolYear);
            model.addObject("person", personCurrent);
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
            String url = Contains.URL + "/api/admin/schoolYear";
            ModelAndView model = new ModelAndView("redirect:" + url);

            model.addObject("successMessage", successMessage);
            return model;
        }else {
            return new ModelAndView("redirect:");
        }
    }

}
