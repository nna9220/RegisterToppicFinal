package com.web.controller.admin;

import com.web.config.CheckRole;
import com.web.dto.request.RegistrationPeriodRequest;
import com.web.entity.Person;
import com.web.entity.RegistrationPeriod;
import com.web.entity.TypeSubject;
import com.web.repository.PersonRepository;
import com.web.repository.RegistrationPeriodRepository;
import com.web.repository.TypeSubjectRepository;
import com.web.service.Admin.RegistrationPeriodService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationPeriodControllerTest {

    @Mock
    private RegistrationPeriodService registrationPeriodService;

    @Mock
    private RegistrationPeriodRepository registrationPeriodRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private TypeSubjectRepository typeSubjectRepository;

    @InjectMocks
    private RegistrationPeriodController registrationPeriodController;

    RegistrationPeriodControllerTest() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllExisted() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the service method
        when(registrationPeriodService.findAll()).thenReturn(mockRegistrationPeriodList());

        // Test the findAllExisted method
        ModelAndView modelAndView = registrationPeriodController.findAllExisted(session);
        assertEquals("QuanLyDotDK", modelAndView.getViewName());
    }

    @Test
    void savePeriod() {
        // Mocking the HttpServletRequest and HttpSession
        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();

        // Mocking the CheckRole.getRoleCurrent
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository and service methods
        when(typeSubjectRepository.findById(anyInt())).thenReturn(Optional.ofNullable(mock(TypeSubject.class)));
        when(registrationPeriodRepository.save(any(RegistrationPeriod.class))).thenReturn(mock(RegistrationPeriod.class));

        // Test the savePeriod method
        ModelAndView modelAndView = registrationPeriodController.savePeriod(session, "periodName", new Date(), new Date(), request);
        assertEquals("redirect:" + request.getHeader("Referer"), modelAndView.getViewName());
    }

    @Test
    void editClass() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository method
        when(registrationPeriodRepository.findById(anyInt())).thenReturn(Optional.ofNullable(mock(RegistrationPeriod.class)));

        // Test the editClass method
        ModelAndView modelAndView = registrationPeriodController.editClass(1, session);
        assertEquals("admin_editPeriod", modelAndView.getViewName());
    }

    @Test
    void updatePeriod() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the repository method
        when(registrationPeriodRepository.findById(anyInt())).thenReturn(Optional.ofNullable(mock(RegistrationPeriod.class)));

        // Test the updatePeriod method
        ModelAndView modelAndView = registrationPeriodController.updatePeriod(1,
                mock(RegistrationPeriodRequest.class), session, "successMessage");
        assertEquals("redirect:" + Contains.URL + "/api/admin/Period", modelAndView.getViewName());
    }

    private List<RegistrationPeriod> mockRegistrationPeriodList() {
        List<RegistrationPeriod> registrationPeriods = new ArrayList<>();
        registrationPeriods.add(new RegistrationPeriod());
        return registrationPeriods;
    }
}