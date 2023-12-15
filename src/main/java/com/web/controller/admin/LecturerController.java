package com.web.controller.admin;

/*import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;*/
import com.web.config.CheckRole;
import com.web.dto.request.StudentRequest;
import com.web.entity.*;
import com.web.mapper.LecturerMapper;
import com.web.dto.request.LecturerRequest;
import com.web.dto.request.PersonRequest;
import com.web.repository.AuthorityRepository;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.service.Admin.LecturerService;
import com.web.service.Admin.PersonService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("api/admin/lecturer")
public class LecturerController {
    private static final Logger logger = LoggerFactory.getLogger(LecturerController.class);

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private LecturerMapper lecturerMapper;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping
    public ModelAndView getAllLecturer(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            List<Authority> listAutho = authorityRepository.findAll();
            ModelAndView modelAndView = new ModelAndView("QuanLyGV");
            List<Lecturer> lecturerList = lecturerService.getAllLec();
            modelAndView.addObject("listLecturer", lecturerList);
            modelAndView.addObject("major", Major.values());
            modelAndView.addObject("authors", listAutho);
            modelAndView.addObject("person", personCurrent);
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public ModelAndView createLecturerAndPerson(     @RequestParam(value = "personId", required = true) String personId,
                                                     @RequestParam(value = "firstName", required = true) String firstName,
                                                     @RequestParam(value = "lastName", required = true) String lastName,
                                                     @RequestParam(value = "email", required = true) String email,
                                                     @RequestParam(value = "gender", required = true) boolean gender,
                                                     @RequestParam(value = "birthDay", required = true) String birthDay,
                                                     @RequestParam(value = "phone", required = true) String phone,
                                                     @RequestParam(value = "major", required = true) Major major,
                                                     @RequestParam(value = "author") Authority author,
                                                     HttpSession session, HttpServletRequest request) {
        Person personCurrent = CheckRole.getRoleCurrent(session, userUtils, personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            /*if (CheckedPermission.isAdmin(personRepository)) {
                //Tạo person*/
            Person newPerson = new Person();
            newPerson.setPersonId(personId);
            newPerson.setFirstName(firstName);
            newPerson.setLastName(lastName);
            newPerson.setUsername(email);
            newPerson.setGender(gender);
            newPerson.setBirthDay(birthDay);
            newPerson.setPhone(phone);
            newPerson.setAuthorities(author);
            newPerson.setStatus(true);
            //newPerson.setRole(RoleName.valueOf("Student"));
            var person = personRepository.save(newPerson);
            System.out.println(person.getPersonId());
            System.out.println(newPerson.getPersonId() + " " + newPerson.getLastName());
            LecturerRequest lecturerRequest = new LecturerRequest();
            lecturerRequest.setLecturerId(personId);
            lecturerRequest.setPerson(person);
            lecturerRequest.setAuthority(author);
            lecturerRequest.setMajor(String.valueOf(major));
            lecturerService.saveLecturer(lecturerRequest);
            String referer = request.getHeader("Referer");
            // Thực hiện redirect trở lại trang trước đó
            System.out.println("Url: " + referer);
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);

        } else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }
    @GetMapping("/{id}")
    public ModelAndView editStudent(@PathVariable String id, HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Lecturer existLecturer = lecturerRepository.findById(id).orElse(null);
            if (existLecturer!=null){
                Person person = personRepository.findById(existLecturer.getLecturerId()).orElse(null);

                List<Authority> listAutho = authorityRepository.findAll();
                ModelAndView modelAndView = new ModelAndView("admin_editLecturer");
                System.out.println(personCurrent.getUsername()+personCurrent.getFirstName());
                modelAndView.addObject("person", person);
                modelAndView.addObject("major", Major.values());
                modelAndView.addObject("lecturer", existLecturer);
                modelAndView.addObject("autho", listAutho);
                return modelAndView;
            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tìm thấy người dùng");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateStudent(@PathVariable String id,@ModelAttribute PersonRequest studentRequest,
                                      HttpSession session, HttpServletRequest request){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Lecturer existLecturer = lecturerRepository.findById(id).orElse(null);
            if (existLecturer!=null){
                System.out.println(id);
                existLecturer.getPerson().setFirstName(studentRequest.getFirstName());
                existLecturer.getPerson().setLastName(studentRequest.getLastName());

                existLecturer.getPerson().setBirthDay(String.valueOf(studentRequest.getBirthDay()));
                existLecturer.getPerson().setPhone(studentRequest.getPhone());
                existLecturer.getPerson().setStatus(studentRequest.isStatus());
                lecturerRepository.save(existLecturer);
                String referer = Contains.URL + "/api/admin/lecturer";
                System.out.println("Url: " + referer);
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);
            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tìm thấy sinh viên");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteStudent(@PathVariable String id, HttpServletRequest request, HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Person editPerson = personRepository.findById(id).orElse(null);
            if (editPerson!=null) {
                editPerson.setStatus(false);
                personRepository.save(editPerson);
                String referer = request.getHeader("Referer");
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);
            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Không tìm thấy student");
                return error;
            }
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

}