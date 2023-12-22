package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.entity.Subject;
import com.web.entity.Task;
import com.web.repository.*;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentAddTaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private StudentAddTaskController studentAddTaskController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNewTask() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the studentRepository.findById and subjectRepository.findById
        when(studentRepository.findById(any())).thenReturn(Optional.of(mock(Student.class)));
        when(subjectRepository.findById(any())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the getNewTask method
        ModelAndView modelAndView = studentAddTaskController.getNewTask(session);
        assertEquals("student_addTask", modelAndView.getViewName());
    }

    @Test
    void getListTask() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the studentRepository.findById and subjectRepository.findById
        when(studentRepository.findById(any())).thenReturn(Optional.of(mock(Student.class)));
        when(subjectRepository.findById(any())).thenReturn(Optional.of(mock(Subject.class)));

        // Test the getListTask method
        ModelAndView modelAndView = studentAddTaskController.getListTask(session);
        assertEquals("QuanLyDeTai", modelAndView.getViewName());
    }

    @Test
    void createTask() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the studentRepository.findById, subjectRepository.findById, studentRepository.findById, and taskRepository.save
        when(studentRepository.findById(any())).thenReturn(Optional.of(mock(Student.class)));
        when(subjectRepository.findById(any())).thenReturn(Optional.of(mock(Subject.class)));
        when(studentRepository.findById(any())).thenReturn(Optional.of(mock(Student.class)));
        when(taskRepository.save(any(Task.class))).thenReturn(mock(Task.class));

        // Test the createTask method
        ModelAndView modelAndView = studentAddTaskController.createTask(session, "requirement",
                new Date(), new Date(), "assignTo", mock(MockHttpServletRequest.class));
        assertEquals("redirect:" + Contains.URL + "/api/student/task/list", modelAndView.getViewName());
    }

    @Test
    void getDetail() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the taskRepository.findById and fileRepository.findAllByTask
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(mock(Task.class)));
        when(fileRepository.findAllByTask(any(Task.class))).thenReturn(new ArrayList<>());

        // Test the getDetail method
        ModelAndView modelAndView = studentAddTaskController.getDetail(session, 1);
        assertEquals("student_detailTask", modelAndView.getViewName());
    }
}