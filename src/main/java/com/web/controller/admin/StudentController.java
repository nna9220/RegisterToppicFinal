package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.CheckRole;
import com.web.dto.request.StudentClassRequest;
import com.web.entity.*;
import com.web.mapper.StudentMapper;
import com.web.dto.request.PersonRequest;
import com.web.dto.request.StudentRequest;
import com.web.repository.PersonRepository;
import com.web.repository.SchoolYearRepository;
import com.web.repository.StudentClassRepository;
import com.web.repository.StudentRepository;
import com.web.service.Admin.PersonService;
import com.web.service.Admin.SchoolYearService;
import com.web.service.Admin.StudentClassService;
import com.web.service.Admin.StudentService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

@RestController
@RequestMapping("/api/admin/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private StudentClassRepository studentClassRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SchoolYearRepository schoolYearRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private StudentClassService studentClassService;
    @Autowired
    private SchoolYearService schoolYearService;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public ModelAndView getAllStudent(HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            List<StudentClass> studentClasses = studentClassService.findAll();
            List<SchoolYear> schoolYears = schoolYearService.findAll();
            List<Student> studentList = studentService.getAllStudent();
            ModelAndView modelAndView = new ModelAndView("QuanLySV");

            modelAndView.addObject("listClass", studentClasses);

            modelAndView.addObject("major", Major.values());
            modelAndView.addObject("listYear", schoolYears);
            modelAndView.addObject("students",studentList);
            System.out.println("Sinh viên: "+ studentList.get(0).getPerson().getFirstName());
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public ModelAndView createStudentAndPerson(     @RequestParam(value = "personId", required = true) String personId,
                                                    @RequestParam(value = "firstName", required = true) String firstName,
                                                    @RequestParam(value = "lastName", required = true) String lastName,
                                                    @RequestParam(value = "email", required = true) String email,
                                                    @RequestParam(value = "gender", required = true) Boolean gender,
                                                    @RequestParam(value = "birthDay", required = true) String birthDay,
                                                    @RequestParam(value = "phone", required = true) String phone,
                                                    @RequestParam(value = "major", required = true) Major major,
                                                    @RequestParam(value = "id") int id,
                                                    @RequestParam(value = "year") int yearId,
                                                    HttpSession session, HttpServletRequest request){

        System.out.println("Class " + id);
        System.out.println("Year" + yearId);
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
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
                Authority authority = new Authority();
                authority.setName("ROLE_STUDENT");
                newPerson.setAuthorities(authority);
                newPerson.setStatus(true);
                //newPerson.setRole(RoleName.valueOf("Student"));
                var person = personRepository.save(newPerson);
                System.out.println(person.getPersonId());
                System.out.println(newPerson.getPersonId() + " " + newPerson.getLastName());
                //Tạo sinh viên -> lấy id từ person vừa tạo
                StudentRequest newStudent = new StudentRequest();
                newStudent.setStudentId(personId);
                newStudent.setPersonId(newPerson);
                newStudent.setMajor(major.name());
                StudentClass existedClass = studentClassRepository.findById(id).orElse(null);
                System.out.println(id);
                if (existedClass!=null){
                    newStudent.setStudentClass(existedClass);
                }
                SchoolYear existedYear = schoolYearRepository.findById(yearId).orElse(null);
                if (existedYear!=null){
                    newStudent.setSchoolYear(existedYear);
                }
                studentService.saveStudent(newStudent);
                String referer = "http://localhost:8080/api/admin/student";
                System.out.println("Url: " + referer);
                // Thực hiện redirect trở lại trang trước đó
                return new ModelAndView("redirect:" + referer);

            }else {
                ModelAndView error = new ModelAndView();
                error.addObject("errorMessage", "Bạn không có quyền truy cập.");
                return error;
            }
    }
    @GetMapping("/{id}")
    public ModelAndView editStudent(@PathVariable String id, HttpSession session){
        Person personCurrent = CheckRole.getRoleCurrent(session,userUtils,personRepository);
        if (personCurrent.getAuthorities().getName().equals("ROLE_ADMIN")) {
            Student existStudent = studentRepository.findById(id).orElse(null);;
            if (existStudent!=null){
                Person person = personRepository.findById(existStudent.getStudentId()).orElse(null);
                List<StudentClass> studentClasses = studentClassService.findAll();
                List<SchoolYear> schoolYears = schoolYearService.findAll();
                ModelAndView modelAndView = new ModelAndView("admin_editStudent");
                System.out.println(personCurrent.getUsername()+personCurrent.getFirstName());
                modelAndView.addObject("student", existStudent);
                modelAndView.addObject("person", person);
                modelAndView.addObject("listClass", studentClasses);
                modelAndView.addObject("major", Major.values());
                modelAndView.addObject("listYear", schoolYears);
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
            Student existStudent = studentRepository.findById(id).orElse(null);
            if (existStudent!=null){
                System.out.println(id);
                existStudent.getPerson().setFirstName(studentRequest.getFirstName());
                existStudent.getPerson().setLastName(studentRequest.getLastName());

                existStudent.getPerson().setBirthDay(String.valueOf(studentRequest.getBirthDay()));
                existStudent.getPerson().setPhone(studentRequest.getPhone());
                existStudent.getPerson().setStatus(studentRequest.isStatus());

                studentRepository.save(existStudent);
                String referer = "http://localhost:8080/api/admin/student";
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
