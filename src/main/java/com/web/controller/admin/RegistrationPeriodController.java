package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.CheckRole;
import com.web.entity.Person;
import com.web.entity.RegistrationPeriod;
import com.web.entity.StudentClass;
import com.web.entity.TypeSubject;
import com.web.mapper.RegistrationPeriodMapper;
import com.web.dto.request.RegistrationPeriodRequest;
import com.web.repository.PersonRepository;
import com.web.repository.RegistrationPeriodRepository;
import com.web.repository.TypeSubjectRepository;
import com.web.service.Admin.RegistrationPeriodService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/Period")
public class RegistrationPeriodController {
    @Autowired
    private RegistrationPeriodService registrationPeriodService;
    @Autowired
    private RegistrationPeriodMapper registrationPeriodMapper;
    @Autowired
    private RegistrationPeriodRepository registrationPeriodRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TypeSubjectRepository typeSubjectRepository;


    @GetMapping
    public ModelAndView findAllExisted(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            ModelAndView modelAndView = new ModelAndView("QuanLyDotDK");
            List<RegistrationPeriod> registrationPeriods = registrationPeriodService.findAll();
            modelAndView.addObject("period",registrationPeriods);
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public ModelAndView savePeriod(HttpSession session, @RequestParam("periodName") String periodName,
                                   @RequestParam("timeStart") Date timeStart,
                                   @RequestParam("timeEnd") Date timeEnd, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            RegistrationPeriod registrationPeriod = new RegistrationPeriod();
            registrationPeriod.setRegistrationName(periodName);
            registrationPeriod.setRegistrationTimeStart(timeStart);
            registrationPeriod.setRegistrationTimeEnd(timeEnd);
            TypeSubject typeSubject = typeSubjectRepository.findById(1).orElse(null);
            registrationPeriod.setTypeSubjectId(typeSubject);
            registrationPeriodRepository.save(registrationPeriod);
            String referer = request.getHeader("Referer");
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }

    }

    @GetMapping("/{periodId}")
    public ModelAndView editClass(@PathVariable int periodId, HttpSession session) {
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            // Lấy thông tin lớp học cần chỉnh sửa từ service
            RegistrationPeriod registrationPeriod = registrationPeriodRepository.findById(periodId).orElse(null);
            // Kiểm tra xem lớp học có tồn tại không
            if (registrationPeriod != null) {
                // Trả về ModelAndView với thông tin lớp học và đường dẫn của trang chỉnh sửa
                ModelAndView model = new ModelAndView("admin_editPeriod");
                model.addObject("period", registrationPeriod);
                return model;
            } else {
                // Trả về ModelAndView với thông báo lỗi nếu không tìm thấy lớp học
                ModelAndView errorModel = new ModelAndView("error");
                errorModel.addObject("errorMessage", "Không tìm thấy lớp học");
                return errorModel;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/edit/{periodId}")
    public ModelAndView updatePeriod(@PathVariable int periodId, @ModelAttribute RegistrationPeriodRequest registrationPeriodRequest,HttpSession session,
                                     @ModelAttribute("successMessage") String successMessage){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            RegistrationPeriod existRegistrationPeriod = registrationPeriodRepository.findById(periodId).orElse(null);
            if (existRegistrationPeriod != null) {
                existRegistrationPeriod.setRegistrationTimeStart(registrationPeriodRequest.getRegistrationTimeStart());
                existRegistrationPeriod.setRegistrationTimeEnd(registrationPeriodRequest.getRegistrationTimeEnd());
                registrationPeriodRepository.save(existRegistrationPeriod);
                String url = "http://localhost:8080/api/admin/Period";
                ModelAndView model = new ModelAndView("redirect:" + url);

                model.addObject("successMessage", successMessage);
                return model;
            } else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "không tìm thấy đợt đăng ký.");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
