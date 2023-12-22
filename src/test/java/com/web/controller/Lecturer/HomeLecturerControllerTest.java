package com.web.controller.Lecturer;

import com.web.config.CheckRole;
import com.web.dto.request.PersonRequest;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.service.Admin.PersonService;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HomeLecturerControllerTest {

    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonService personService;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private HomeLecturerController homeLecturerController;
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
        ModelAndView modelAndView = homeLecturerController.getHome(session, "token", mock(HttpServletRequest.class));
        assertEquals("Dashboard_GiangVien", modelAndView.getViewName());
        verify(modelAndView).addObject("token", "token");
    }


    @Test
    void getProfile() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the getProfile method
        ModelAndView modelAndView = homeLecturerController.getProfile(session);
        assertEquals("profileGV", modelAndView.getViewName());
    }



    @Test
    void getEditProfile() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the getEditProfile method
        ModelAndView modelAndView = homeLecturerController.getEditProfile(session);
        assertEquals("profileGV", modelAndView.getViewName());
    }



    @Test
    void updateLecturer() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the updateLecturer method
        ModelAndView modelAndView = homeLecturerController.updateLecturer("1", mock(PersonRequest.class), session, mock(HttpServletRequest.class));
        assertEquals("redirect:" + Contains.URL + "/api/lecturer/profile", modelAndView.getViewName());
    }
}

