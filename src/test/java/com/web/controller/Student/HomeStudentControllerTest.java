package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.dto.request.PersonRequest;
import com.web.entity.Authority;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.repository.PersonRepository;
import com.web.repository.StudentRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeStudentControllerTest {
    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private HomeStudentController homeStudentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getListSubject() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        Person person = new Person();
        person.setAuthorities(new Authority("ROLE_STUDENT"));
        when(personRepository.findByUsername(any())).thenReturn(Optional.of(person));

        // Act
        ModelAndView modelAndView = homeStudentController.getListSubject(session);

        // Assert
        assertEquals("Dashboard_SinhVien", modelAndView.getViewName());
        assertEquals(person, modelAndView.getModel().get("person"));
    }


    @Test
    void getProfile() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        Person person = new Person();
        person.setAuthorities(new Authority("ROLE_STUDENT"));
        when(personRepository.findByUsername(any())).thenReturn(Optional.of(person));

        // Act
        ModelAndView modelAndView = homeStudentController.getProfile(session);

        // Assert
        assertEquals("profileSV", modelAndView.getViewName());
        assertEquals(person, modelAndView.getModel().get("person"));
    }

    @Test
    void getEditProfile() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        Person person = new Person();
        person.setAuthorities(new Authority("ROLE_STUDENT"));
        when(personRepository.findByUsername(any())).thenReturn(Optional.of(person));

        // Act
        ModelAndView modelAndView = homeStudentController.getEditProfile(session);

        // Assert
        assertEquals("student_editprofile", modelAndView.getViewName());
        assertEquals(person, modelAndView.getModel().get("person"));
    }


    @Test
    void updateStudent() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        Person person = new Person();
        person.setAuthorities(new Authority("ROLE_STUDENT"));
        Student existStudent = new Student();
        existStudent.setPerson(person);
        when(CheckRole.getRoleCurrent(session, userUtils, personRepository)).thenReturn(person);
        when(studentRepository.findById(any())).thenReturn(Optional.of(existStudent));

        // Act
        ModelAndView modelAndView = homeStudentController.updateStudent("1", new PersonRequest(), session, request);

        // Assert
        assertEquals("redirect:" + Contains.URL + "/api/student/profile", modelAndView.getViewName());
    }
}

