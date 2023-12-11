package com.web.controller.admin;

//import hcmute.edu.vn.registertopic_be.authentication.CheckedPermission;
import com.web.config.CheckRole;
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
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            modelAndView.addObject("students",studentMapper.toStudentListDTO(studentList));
            return modelAndView;
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PostMapping("/create")
    public ModelAndView createStudentAndPerson(@RequestParam(value = "personId", required = true) String personId,
                                               @RequestParam(value = "firstName", required = true) String firstName,
                                               @RequestParam(value = "lastName", required = true) String lastName,
                                               @RequestParam(value = "email", required = true) String email,
                                               @RequestParam(value = "gender", required = true) Boolean gender,
                                               @RequestParam(value = "birthDay", required = true) String birthDay,
                                               @RequestParam(value = "phone", required = true) String phone,
                                               @RequestParam(value = "major", required = true) Major major,
                                               @ModelAttribute(value = "classId") StudentClass classId,
                                               @ModelAttribute(value = "year") SchoolYear yearId,
                                               HttpSession session, HttpServletRequest request){

        System.out.println("Class " + classId);
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
            newStudent.setPersonId(person);
            newStudent.setMajor(major.name());
            List<Student> addStudent = new ArrayList<>();
            addStudent.add(studentMapper.toEntity(newStudent));
            //Tìm class với id tương ứng

            newStudent.setStudentClass(classId);
            classId.setStudents(addStudent);
            studentClassRepository.save(classId);
            //Tìm year với id tương ứng.

            newStudent.setSchoolYear(yearId);
            yearId.setStudents(addStudent);
            System.out.println("Year: " + yearId.getStudents().get(0).getStudentId());
            System.out.println(yearId.getStudents().get(0).getStudentId());
            schoolYearRepository.save(yearId);

            for (Student x : yearId.getStudents()) {
                System.out.println(x.getStudentId());
                System.out.println(yearId.getStudents().size());
            }
            for (Student x : yearId.getStudents()) {
                System.out.println("Class " + x.getStudentId());
                System.out.println(yearId.getStudents().size());
            }
            Student saveStudent = studentService.saveStudent(newStudent);
            System.out.println(saveStudent.getPerson() + "" + newStudent.getPersonId());
            String referer = "http://localhost:8080/api/admin/student";
            System.out.println("Url: " + referer);
            // Thực hiện redirect trở lại trang trước đó
            return new ModelAndView("redirect:" + referer);
           /* } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }*/
        }else {
            ModelAndView error = new ModelAndView();
            error.addObject("errorMessage", "Bạn không có quyền truy cập.");
            return error;
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody PersonRequest request){
        return new ResponseEntity<>(personService.editPerson(id,request), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id){
        return new ResponseEntity<>(personService.deletePerson(id), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public  ResponseEntity<?> getDetail(@PathVariable String id){
        return ResponseEntity.ok(studentService.detail(id));
    }
}