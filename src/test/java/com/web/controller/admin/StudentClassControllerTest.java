package com.web.controller.admin;

import com.web.config.CheckRole;
import com.web.dto.request.StudentClassRequest;
import com.web.entity.Person;
import com.web.entity.StudentClass;
import com.web.repository.PersonRepository;
import com.web.repository.StudentClassRepository;
import com.web.service.Admin.StudentClassService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StudentClassControllerTest {

    @Mock
    private StudentClassService studentClassService;

    @Mock
    private StudentClassRepository studentClassRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private StudentClassController studentClassController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStudentClass() {
        //Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the service method
        when(studentClassService.findAll()).thenReturn(mockStudentClassList());

        // Test the getStudentClass method
        ModelAndView modelAndView = studentClassController.getStudentClass(session);
        assertEquals("QuanLyLopHoc", modelAndView.getViewName());
    }

    @Test
    void saveClass() {
        // Mocking the HttpServletRequest and RedirectAttributes
        HttpServletRequest request = new MockHttpServletRequest();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Mocking the service method
        doNothing().when(studentClassService).createStudentClass(any(StudentClassRequest.class));

        // Test the saveClass method
        ModelAndView modelAndView = studentClassController.saveClass("ClassName", request, redirectAttributes);
        assertEquals("redirect:" + request.getHeader("Referer"), modelAndView.getViewName());
    }

    
    @Test
    void editClass() {
    }

    @Test
    void updateStudentClass() {
        // Mocking the repository method
        when(studentClassService.getStudentClassById(anyInt())).thenReturn(mock(StudentClass.class));

        // Test the updateStudentClass method
        ModelAndView modelAndView = studentClassController.updateStudentClass(1, mock(StudentClassRequest.class), "successMessage");
        assertEquals("redirect:" + Contains.URL + "/api/admin/studentClass", modelAndView.getViewName());
    }

    private List<StudentClass> mockStudentClassList() {
        List<StudentClass> studentClasses = new ArrayList<>();
        studentClasses.add(new StudentClass());
        return studentClasses;
    }
}