package com.web.controller.admin;

import com.web.config.CheckRole;
import com.web.dto.request.SchoolYearRequest;
import com.web.entity.Person;
import com.web.entity.SchoolYear;
import com.web.repository.PersonRepository;
import com.web.repository.SchoolYearRepository;
import com.web.service.Admin.SchoolYearService;
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

class SchoolYearControllerTest {

    @Mock
    private SchoolYearService schoolYearService;

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private SchoolYearController schoolYearController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllSchoolYear() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the service method
        when(schoolYearService.findAll()).thenReturn(mockSchoolYearList());

        // Test the getAllSchoolYear method
        ModelAndView modelAndView = schoolYearController.getAllSchoolYear(session);
        assertEquals("QuanLyNienKhoa", modelAndView.getViewName());
    }


    @Test
    void saveSchoolYear() {
        // Mocking the HttpServletRequest
        HttpServletRequest request = new MockHttpServletRequest();

        // Mocking the repository and service methods
        when(schoolYearService.createSchoolYear(any(SchoolYearRequest.class))).thenReturn(mock(SchoolYear.class));

        // Test the saveSchoolYear method
        ModelAndView modelAndView = schoolYearController.saveSchoolYear("2023-2024", request);
        assertEquals("redirect:" + request.getHeader("Referer"), modelAndView.getViewName());
    }

    @Test
    void editClass() {
        // Mocking the repository method
        when(schoolYearRepository.findById(anyInt())).thenReturn(Optional.ofNullable(mock(SchoolYear.class)));

        // Test the editClass method
        ModelAndView modelAndView = schoolYearController.editClass(1);
        assertEquals("admin_editYear", modelAndView.getViewName());
    }


    @Test
    void updateYear() {
        // Mocking the repository method
        when(schoolYearRepository.findById(anyInt())).thenReturn(Optional.ofNullable(mock(SchoolYear.class)));

        // Test the updateYear method
        ModelAndView modelAndView = schoolYearController.updateYear(1, mock(SchoolYearRequest.class), "successMessage");
        assertEquals("redirect:" + Contains.URL + "/api/admin/schoolYear", modelAndView.getViewName());
    }

    private List<SchoolYear> mockSchoolYearList() {
        List<SchoolYear> schoolYears = new ArrayList<>();
        schoolYears.add(new SchoolYear());
        return schoolYears;
    }
}