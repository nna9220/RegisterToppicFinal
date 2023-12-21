package com.web.controller.admin;

import com.web.config.CheckRole;
import com.web.dto.request.PersonRequest;
import com.web.entity.*;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentControllerTest {

    @Mock
    private StudentClassRepository studentClassRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentClassService studentClassService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private PersonService personService;

    @Mock
    private UserUtils userUtils;

    @Mock
    private SchoolYearService schoolYearService;

    @InjectMocks
    private StudentController studentController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllStudent() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the service methods
        when(studentClassService.findAll()).thenReturn(mockStudentClassList());
        when(schoolYearService.findAll()).thenReturn(mockSchoolYearList());
        when(studentService.getAllStudent()).thenReturn(mockStudentList());

        // Test the getAllStudent method
        ModelAndView modelAndView = studentController.getAllStudent(session);
        assertEquals("QuanLySV", modelAndView.getViewName());
    }

    @Test
    void createStudentAndPerson() {
        // Mocking the HttpServletRequest and HttpSession
        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();

        // Mocking the CheckRole.getRoleCurrent
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the service methods
        when(personRepository.save(any(Person.class))).thenReturn(mock(Person.class));
        when(studentService.saveStudent(any())).thenReturn(mock(Student.class));

        // Test the createStudentAndPerson method
        ModelAndView modelAndView = studentController.createStudentAndPerson("personId", "firstName", "lastName",
                "email", true, "birthDay", "phone", Major.AnToanThongTin, 1, 2, session, request);

        assertEquals("redirect:" + Contains.URL + "/api/admin/student", modelAndView.getViewName());
    }

    @Test
    void editStudent() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository method
        when(studentRepository.findById(anyString())).thenReturn(Optional.ofNullable(mock(Student.class)));

        // Test the editStudent method
        ModelAndView modelAndView = studentController.editStudent("studentId", session);
        assertEquals("admin_editStudent", modelAndView.getViewName());
    }

    @Test
    void updateStudent() {
        // Mocking the HttpServletRequest and HttpSession
        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();

        // Mocking the CheckRole.getRoleCurrent
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository method
        when(studentRepository.findById(anyString())).thenReturn(Optional.ofNullable(mock(Student.class)));

        // Test the updateStudent method
        ModelAndView modelAndView = studentController.updateStudent("studentId", mock(PersonRequest.class), session, request);
        assertEquals("redirect:" + Contains.URL + "/api/admin/student", modelAndView.getViewName());
    }

    @Test
    void deleteStudent() {
        // Mocking the HttpServletRequest and HttpSession
        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();

        // Mocking the CheckRole.getRoleCurrent
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository method
        when(personRepository.findById(anyString())).thenReturn(Optional.ofNullable(mock(Person.class)));

        // Test the deleteStudent method
        ModelAndView modelAndView = studentController.deleteStudent("studentId", request, session);
        assertEquals("redirect:" + request.getHeader("Referer"), modelAndView.getViewName());
    }

    private List<StudentClass> mockStudentClassList() {
        List<StudentClass> studentClasses = new ArrayList<>();
        studentClasses.add(new StudentClass());
        return studentClasses;
    }

    private List<SchoolYear> mockSchoolYearList() {
        List<SchoolYear> schoolYears = new ArrayList<>();
        schoolYears.add(new SchoolYear());
        return schoolYears;
    }

    private List<Student> mockStudentList() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        return students;
    }
}