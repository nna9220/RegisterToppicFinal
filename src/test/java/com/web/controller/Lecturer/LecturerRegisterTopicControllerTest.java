package com.web.controller.Lecturer;

import com.web.config.CheckRole;
import com.web.entity.*;
import com.web.mapper.SubjectMapper;
import com.web.repository.*;
import com.web.service.Admin.StudentService;
import com.web.service.Admin.SubjectService;
import com.web.service.Lecturer.LecturerSubjectService;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LecturerRegisterTopicControllerTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubjectService subjectService;

    @Mock
    private SubjectMapper subjectMapper;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private LecturerSubjectService lecturerSubjectService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private TypeSubjectRepository typeSubjectRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private RegistrationPeriodRepository registrationPeriodRepository;

    @InjectMocks
    private LecturerRegisterTopicController lecturerRegisterTopicController;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getQuanLyDeTai() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Mocking the subjectRepository.findSubjectByLecturerIntro
        when(subjectRepository.findSubjectByLecturerIntro(any(Lecturer.class))).thenReturn(Collections.singletonList(mock(Subject.class)));

        // Test the getQuanLyDeTai method
        ModelAndView modelAndView = lecturerRegisterTopicController.getQuanLyDeTai(session);
        assertEquals("QuanLyDeTai_GV", modelAndView.getViewName());
    }

    @Test
    void lecturerRegisterTopic() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Mocking the studentRepository.findById
        when(studentRepository.findById(any())).thenReturn(Optional.of(mock(Student.class)));

        // Mocking the typeSubjectRepository.findById
        when(typeSubjectRepository.findById(any())).thenReturn(Optional.of(mock(TypeSubject.class)));

        // Test the lecturerRegisterTopic method
        ModelAndView modelAndView = lecturerRegisterTopicController.lecturerRegisterTopic("name", "requirement", "expected",
                "student1", "student2", session, mock(MockHttpServletRequest.class));
        assertEquals("redirect:" + Contains.URL + "/api/lecturer/subject", modelAndView.getViewName());
    }

    @Test
    void getListTask() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Mocking the subjectRepository.findById
        when(subjectRepository.findById(any())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the getListTask method
        ModelAndView modelAndView = lecturerRegisterTopicController.getListTask(session, 1);
        assertEquals("lecturer_listTask", modelAndView.getViewName());
    }

    @Test
    void getDetail() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the lecturerRepository.findById
        when(lecturerRepository.findById(any())).thenReturn(Optional.of(mock(Lecturer.class)));

        // Mocking the taskRepository.findById
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(mock(Task.class)));

        // Mocking the fileRepository.findAll
        when(fileRepository.findAll()).thenReturn(Collections.singletonList(mock(FileComment.class)));

        // Test the getDetail method
        ModelAndView modelAndView = lecturerRegisterTopicController.getDetail(session, 1);
        assertEquals("lecturer_detailTask", modelAndView.getViewName());
    }
}