package com.web.controller.HeadOfDepartment;

import com.web.config.CheckRole;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Subject;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.SubjectService;
import com.web.service.ReportService;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddCounterArgumentControllerTest {

    @Mock
    private SubjectService subjectService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private AddCounterArgumentController addCounterArgumentController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateExcelReport() throws Exception {
        // Mocking the HttpSession
        MockHttpSession session = new MockHttpSession();

        // Mocking the response
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(mockServletOutputStream());

        // Test the generateExcelReport method
        addCounterArgumentController.generateExcelReport(response, session);
        verify(response, times(1)).setContentType("application/octet-stream");
        verify(response, times(1)).setHeader(eq("Content-Disposition"), anyString());
        verify(reportService, times(1)).generateExcel(eq(response), eq(session));
        verify(response, times(1)).flushBuffer();
    }

    @Test
    void getAddCounterArgument() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById and subjectRepository.findById
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(mock(Lecturer.class)));
        when(subjectRepository.findById(anyInt())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the getAddCounterArgument method
        ModelAndView modelAndView = addCounterArgumentController.getAddCounterArgument(1, session);
        assertEquals("ListLecturerAddCounterArgument", modelAndView.getViewName());
    }

    @Test
    void getListSubjectAddCounterArgument() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the getListSubjectAddCounterArgument method
        ModelAndView modelAndView = addCounterArgumentController.getListSubjectAddCounterArgument(session);
        assertEquals("PhanGVPhanBien", modelAndView.getViewName());
    }

    @Test
    void addCounterArgumrnt() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById and subjectRepository.findById
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(mock(Lecturer.class)));
        when(subjectRepository.findById(anyInt())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the addCounterArgument method
        ModelAndView modelAndView = addCounterArgumentController.addCounterArgumrnt(1, session, "lecturerId");
        assertEquals("redirect:/api/head/subject/listAdd", modelAndView.getViewName());

    }

    @Test
    void getDanhSachDeTai() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Test the getDanhSachDeTai method
        ModelAndView modelAndView = addCounterArgumentController.getDanhSachDeTai(session);
        assertEquals("Duyet_TBM", modelAndView.getViewName());
    }

    @Test
    void lecturerRegisterTopic() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById and subjectRepository.findById
        when(lecturerRepository.findById(anyString())).thenReturn(Optional.of(mock(Lecturer.class)));
        when(subjectRepository.findById(anyInt())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the lecturerRegisterTopic method
        ModelAndView modelAndView = addCounterArgumentController.lecturerRegisterTopic("name", "requirement",
                "expected", "student1", "student2", session, mock(HttpServletRequest.class));
        assertEquals("redirect:/api/head/subject", modelAndView.getViewName());
    }

    @Test
    void browseSubject() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Test the browseSubject method
        ModelAndView modelAndView = addCounterArgumentController.browseSubject(1, session, mock(HttpServletRequest.class));
        assertEquals("redirect:/api/head/subject", modelAndView.getViewName());
    }

    private ServletOutputStream mockServletOutputStream() {
        return mock(ServletOutputStream.class);
    }
}