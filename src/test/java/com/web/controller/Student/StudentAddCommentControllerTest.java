package com.web.controller.Student;

import com.web.config.CheckRole;
import com.web.entity.Comment;
import com.web.entity.Person;
import com.web.entity.Task;
import com.web.repository.CommentRepository;
import com.web.repository.PersonRepository;
import com.web.repository.TaskRepository;
import com.web.service.FileMaterialService;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentAddCommentControllerTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private FileMaterialService fileMaterialService;

    @InjectMocks
    private StudentAddCommentController studentAddCommentController;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createComment() {
        // Mocking the HttpSession and CheckRole.getRoleCurrent
        MockHttpSession session = new MockHttpSession();
        when(CheckRole.getRoleCurrent(eq(session), any(UserUtils.class), any(PersonRepository.class))).thenReturn(mock(Person.class));

        // Mocking the taskRepository.findById, commentRepository.save, and fileMaterialService.storeFile
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(mock(Task.class)));
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));
        when(fileMaterialService.storeFile(any())).thenReturn("filename");

        // Test the createComment method
        ModelAndView modelAndView = studentAddCommentController.createComment(1, "content", createMockMultipartFile(), session);
        assertEquals("redirect:/api/student/task/detail/1", modelAndView.getViewName());
    }

    @Test
    void downloadFile() {
        // Mocking the fileMaterialService.loadFileAsResource
        when(fileMaterialService.loadFileAsResource(anyString())).thenReturn(mockResource());

        // Test the downloadFile method
        ResponseEntity<Resource> responseEntity = studentAddCommentController.downloadFile("filename", mockHttpServletRequest());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    private MockHttpServletRequest mockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletResponse mockHttpServletResponse() {
        return new MockHttpServletResponse();
    }

    private Resource mockResource() {
        return mock(Resource.class);
    }
}