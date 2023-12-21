package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.Person;
import com.web.entity.Student;
import com.web.repository.PersonRepository;
import com.web.repository.StudentRepository;
import com.web.repository.SubjectRepository;
import com.web.service.Admin.StudentService;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentRegisterTopicTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private StudentRegisterTopic studentRegisterTopic;

    @Test
    void testGetListSubject() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        HttpSession session = mock(HttpSession.class);
        //when(CheckRole.getRoleCurrent((HttpSession) eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the studentRepository.findById
        Student student = mock(Student.class);
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        // Mocking the subjectRepository.findSubjectByStatusAndMajorAndStudent
        Subject subject = mock(Subject.class);
        when(subjectRepository.findSubjectByStatusAndMajorAndStudent(eq(true), any()));

        // Test the getListSubject method
        ModelAndView modelAndView = studentRegisterTopic.getListSubject(session);
        assertEquals("QuanLyDeTai_SV", modelAndView.getViewName());
        verify(modelAndView).addObject("subjectList", Collections.singletonList(subject));
    }

    @Test
    void testRegisterTopic() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        HttpSession session = mock(HttpSession.class);
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the studentRepository.findById
        Student student = mock(Student.class);
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        // Mocking the subjectRepository.findById
        Subject subject = mock(Subject.class);
        when(subjectRepository.findById(anyInt()));

        // Test the registerTopic method
        ModelAndView modelAndView = studentRegisterTopic.registerTopic(1, session, mock(HttpServletRequest.class));
        assertEquals("redirect:", modelAndView.getViewName()); // Check if it redirects, adjust as needed
    }
}

