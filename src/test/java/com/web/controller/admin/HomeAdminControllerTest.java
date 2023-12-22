package com.web.controller.admin;

import com.web.dto.request.PersonRequest;
import com.web.entity.Authority;
import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeAdminControllerTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private HomeAdminController homeAdminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getHome_AdminRole_ReturnsDashboard() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/admin/home");

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getHome(session, request);

        // Assert
        assertEquals("Dashboard_Admin", result.getViewName());
        assertTrue(result.getModel().containsKey("person"));
        assertEquals(personCurrent, result.getModel().get("person"));
    }


    @Test
    void getHome_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/admin/home");

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getHome(session, request);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void getProfile_AdminRole_ReturnsProfileAdmin() {
        // Arrange
        HttpSession session = new MockHttpSession();

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getProfile(session);

        // Assert
        assertEquals("profileAdmin", result.getViewName());
        assertTrue(result.getModel().containsKey("person"));
        assertEquals(personCurrent, result.getModel().get("person"));
    }

    @Test
    void getProfile_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getProfile(session);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void getEditProfile_AdminRole_ReturnsProfileAdmin() {
        // Arrange
        HttpSession session = new MockHttpSession();

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getEditProfile(session);

        // Assert
        assertEquals("profileAdmin", result.getViewName());
        assertTrue(result.getModel().containsKey("person"));
        assertEquals(personCurrent, result.getModel().get("person"));
    }

    @Test
    void getEditProfile_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        // Act
        ModelAndView result = homeAdminController.getEditProfile(session);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void updateLecturer_AdminRole_ReturnsRedirect() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/admin/edit/1");

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("John");
        personRequest.setLastName("Doe");
        //personRequest.setBirthDay('1990/12/12');
        personRequest.setPhone("123456789");
        personRequest.setStatus(true);

        // Act
        ModelAndView result = homeAdminController.updateLecturer("1", personRequest, session, request);

        // Assert
        assertEquals("redirect:" + Contains.URL + "/api/admin/profile", result.getViewName());
    }

    @Test
    void updateLecturer_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/admin/edit/1");

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority());

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("John");
        personRequest.setLastName("Doe");
        //personRequest.setBirthDay("1990-01-01");
        personRequest.setPhone("123456789");
        personRequest.setStatus(true);

        // Act
        ModelAndView result = homeAdminController.updateLecturer("1", personRequest, session, request);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }
}