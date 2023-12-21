package com.web.controller.admin;

import com.web.dto.request.PersonRequest;
import com.web.entity.Authority;
import com.web.entity.Lecturer;
import com.web.entity.Major;
import com.web.entity.Person;
import com.web.repository.AuthorityRepository;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.service.Admin.LecturerService;
import com.web.utils.UserUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static springfox.documentation.builders.PathSelectors.any;

class LecturerControllerTest {
    @Mock
    private LecturerService lecturerService;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private LecturerController lecturerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllLecturer_AdminRole_ReturnsQuanLyGV() {
        // Arrange
        HttpSession session = new MockHttpSession();
        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(lecturerRepository.findAll()).thenReturn(new ArrayList<>());
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.getAllLecturer(session);

        // Assert
        assertEquals("QuanLyGV", result.getViewName());
        assertTrue(result.getModel().containsKey("listLecturer"));
        assertTrue(result.getModel().containsKey("major"));
        assertTrue(result.getModel().containsKey("authors"));
        assertTrue(result.getModel().containsKey("person"));
        assertEquals(personCurrent, result.getModel().get("person"));
    }

    @Test
    void getAllLecturer_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_LECTURER"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        when(lecturerRepository.findAll()).thenReturn(new ArrayList<>());
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.getAllLecturer(session);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void createLecturerAndPerson_AdminRole_ReturnsRedirect() {
        // Arrange
    }

    @Test
    void createLecturerAndPerson_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("/api/admin/lecturer");

        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority());

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.createLecturerAndPerson("1", "John", "Doe", "john.doe@example.com",
                true, "1990-01-01", "123456789", Major.AnToanThongTin, new Authority(), session, request);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void editStudent_AdminRole_ReturnsAdminEditLecturer() {
        // Arrange
        HttpSession session = new MockHttpSession();
        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(new Lecturer()));

        // Act
        ModelAndView result = lecturerController.editStudent("1", session);

        // Assert
        assertEquals("admin_editLecturer", result.getViewName());
        assertTrue(result.getModel().containsKey("person"));
        assertTrue(result.getModel().containsKey("major"));
        assertTrue(result.getModel().containsKey("lecturer"));
        assertTrue(result.getModel().containsKey("autho"));
    }

    @Test
    void editStudent_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority());

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.editStudent("1", session);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void updateStudent_AdminRole_ReturnsRedirect() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("/api/admin/lecturer");

        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(new Lecturer()));

        // Act
        ModelAndView result = lecturerController.updateStudent("1", new PersonRequest(), session, request);

        // Assert
        assertEquals("redirect:/api/admin/lecturer", result.getViewName());
    }

    @Test
    void updateStudent_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("/api/admin/lecturer");

        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority());

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.updateStudent("1", new PersonRequest(), session, request);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }

    @Test
    void deleteStudent_AdminRole_ReturnsRedirect() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("/api/admin/lecturer");

        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority("ROLE_ADMIN"));

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority("ROLE_ADMIN"));

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);
        when(personRepository.findById(anyString())).thenReturn(Optional.of(new Person()));

        // Act
        ModelAndView result = lecturerController.deleteStudent("1", request, session);

        // Assert
        assertEquals("redirect:/api/admin/lecturer", result.getViewName());
    }

    @Test
    void deleteStudent_NonAdminRole_ReturnsError() {
        // Arrange
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("/api/admin/lecturer");

        List<Authority> listAutho = new ArrayList<>();
        listAutho.add(new Authority());

        Person personCurrent = new Person();
        personCurrent.setAuthorities(new Authority());

        when(userUtils.loadUserByUsername(anyString())).thenReturn(new User(personCurrent.getUsername(), "", new ArrayList<>()));
        when(personRepository.findById(anyString())).thenReturn(Optional.of(personCurrent));
        when(authorityRepository.findAll()).thenReturn(listAutho);

        // Act
        ModelAndView result = lecturerController.deleteStudent("1", request, session);

        // Assert
        assertEquals("error", result.getViewName());
        assertTrue(result.getModel().containsKey("errorMessage"));
        assertEquals("Bạn không có quyền truy cập.", result.getModel().get("errorMessage"));
    }
}