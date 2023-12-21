package com.web.service.Admin;

import com.web.exception.NotFoundException;
import com.web.repository.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {

    }

    @Test
    void browseSubject() {
    }


    @Test
    void browseSubject_NotFound() {
        // Mock data
        int subjectId = 1;

        // Mock repository behavior for a non-existent subject
        when(subjectRepository.findById(subjectId)).thenReturn(java.util.Optional.empty());

        // Call the method under test and expect NotFoundException
        assertThrows(NotFoundException.class, () -> subjectService.browseSubject(subjectId));

        // Verify that repository methods were called
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(subjectRepository, never()).save(any());
    }
}