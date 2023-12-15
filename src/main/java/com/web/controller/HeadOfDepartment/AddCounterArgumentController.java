package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Subject;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.SubjectService;
import com.web.service.ReportService;
import com.web.utils.ExcelUtils;
import com.web.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/head/subject")
@RequiredArgsConstructor
public class AddCounterArgumentController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ReportService reportService;
    @GetMapping("/export")
    public void generateExcelReport(HttpServletResponse response, HttpSession session) throws Exception{

        response.setContentType("application/octet-stream");
        LocalDate nowDate = LocalDate.now();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=subject_" +nowDate+ ".xls";

        response.setHeader(headerKey, headerValue);

        reportService.generateExcel(response, session);
        response.flushBuffer();
    }


    /*@GetMapping("/export")
    public ResponseEntity<Resource> exportCustomer() throws Exception {
        List<Subject> subjectList = subjectRepository.findAll();

        if (!CollectionUtils.isEmpty(subjectList)) {

            LocalDate nowDate = LocalDate.now();
            String fileName = "SubjectExport"+ ".xlsx";

            ByteArrayInputStream in = ExcelUtils.exportSubject(subjectList, fileName);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");
        }
    }*/

    @GetMapping("/listLecturer/{subjectId}")
    public ModelAndView getAddCounterArgument(@PathVariable int subjectId,HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Subject currentSubject = subjectRepository.findById(subjectId).orElse(null);
            Lecturer existedLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView model = new ModelAndView("ListLecturerAddCounterArgument");
            List<Lecturer> lecturerList = lecturerRepository.getListLecturerNotCurrent(existedLecturer.getLecturerId());
            model.addObject("listLecturer",lecturerList);
            model.addObject("subject",currentSubject);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping("/listAdd")
    public ModelAndView getListSubjectAddCounterArgument(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Lecturer existedLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView model = new ModelAndView("PhanGVPB");
            List<Subject> subjectByCurrentLecturer = subjectRepository.findSubjectByAsisAdvisorAndMajor(true,existedLecturer.getMajor());
            model.addObject("listSubject",subjectByCurrentLecturer);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/addCounterArgumrnt/{subjectId}/{lecturerId}")
    public ModelAndView addCounterArgumrnt(@PathVariable int subjectId, HttpSession session, @PathVariable String lecturerId){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Subject existedSubject = subjectRepository.findById(subjectId).orElse(null);
            if (existedSubject != null) {
                Lecturer currentLecturer = lecturerRepository.findById(lecturerId).orElse(null);
                List<Subject> addSub = new ArrayList<>();
                addSub.add(existedSubject);
                if (currentLecturer != null) {
                    currentLecturer.setListSubCounterArgument(addSub);
                    existedSubject.setThesisAdvisorId(currentLecturer);
                    lecturerRepository.save(currentLecturer);
                    subjectRepository.save(existedSubject);
                }
            }
            String referer = "http://localhost:5000/api/head/subject/listAdd";
            // Thực hiện redirect trở lại trang trước đó
            System.out.println("Url: " + referer);
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @GetMapping
    public ModelAndView getDanhSachDeTai(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD")) {
            Lecturer existedLecturer = lecturerRepository.findById(personCurrent.getPersonId()).orElse(null);
            ModelAndView model = new ModelAndView("Duyet_TBM");
            List<Subject> subjectByCurrentLecturer = subjectRepository.findSubjectByStatusAndMajor(false,existedLecturer.getMajor());
            model.addObject("listSubject",subjectByCurrentLecturer);
            return model;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }



    @PostMapping("/browse/{id}")
    public ModelAndView browseSubject(@PathVariable int id, HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_HEAD") ) {
            subjectService.browseSubject(id);
            String referer = "http://localhost:5000/api/head/subject";
            // Thực hiện redirect trở lại trang trước đó
            System.out.println("Url: " + referer);
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
}
