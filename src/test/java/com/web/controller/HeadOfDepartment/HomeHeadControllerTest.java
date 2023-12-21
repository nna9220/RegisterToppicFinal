package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.dto.request.PersonRequest;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeHeadControllerTest {

    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private LecturerRepository lecturerRepository;

    @InjectMocks
    private HomeHeadController homeHeadController;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getHome() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Test the getHome method
        ModelAndView modelAndView = homeHeadController.getHome(session);
        assertEquals("Dashboard_TBM", modelAndView.getViewName());
    }

    @Test
    void getProfile() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the getProfile method
        ModelAndView modelAndView = homeHeadController.getProfile(session);
        assertEquals("profileTBM", modelAndView.getViewName());
    }

    @Test
    void getEditProfile() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the personRepository.findById
        when(personRepository.findById(any())).thenReturn(Optional.of(mock(Person.class)));

        // Test the getEditProfile method
        ModelAndView modelAndView = homeHeadController.getEditProfile(session);
        assertEquals("profileTBM", modelAndView.getViewName());
    }

    @Test
    void updateLecturer() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the personRepository.findById
        when(personRepository.findById(any())).thenReturn(Optional.of(mock(Person.class)));

        // Test the updateLecturer method
        ModelAndView modelAndView = homeHeadController.updateLecturer("1", mock(PersonRequest.class), session, mock(MockHttpServletRequest.class));
        assertEquals("redirect:" + Contains.URL + "/api/head/profile", modelAndView.getViewName());
    }
}