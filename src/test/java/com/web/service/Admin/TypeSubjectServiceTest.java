package com.web.service.Admin;

import com.web.dto.request.TypeSubjectRequest;
import com.web.entity.TypeSubject;
import com.web.mapper.TypeSubjectMapper;
import com.web.repository.TypeSubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeSubjectServiceTest {
    @Mock
    private TypeSubjectRepository typeSubjectRepository;

    @Mock
    private TypeSubjectMapper typeSubjectMapper;

    @InjectMocks
    private TypeSubjectService typeSubjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        // Giả lập dữ liệu trả về từ repository
        List<TypeSubject> mockTypeSubjects = new ArrayList<>();
        when(typeSubjectRepository.getAllTypeSubject()).thenReturn(mockTypeSubjects);

        // Gọi phương thức và kiểm tra kết quả
        List<TypeSubject> result = typeSubjectService.findAll();
        assertEquals(mockTypeSubjects, result);

        // Kiểm tra xem repository.getAllTypeSubject đã được gọi hay chưa
        verify(typeSubjectRepository, times(1)).getAllTypeSubject();
    }

    @Test
    void createTypeSubject() {
        // Giả lập dữ liệu đầu vào và đối tượng TypeSubject trả về từ repository
        TypeSubjectRequest typeSubjectRequest = new TypeSubjectRequest();
        TypeSubject mockTypeSubject = new TypeSubject();
        when(typeSubjectMapper.toEntity(typeSubjectRequest)).thenReturn(mockTypeSubject);
        when(typeSubjectRepository.save(mockTypeSubject)).thenReturn(mockTypeSubject);

        // Gọi phương thức và kiểm tra kết quả
        TypeSubject result = typeSubjectService.createTypeSubject(typeSubjectRequest);
        assertEquals(mockTypeSubject, result);

        // Kiểm tra xem typeSubjectMapper.toEntity và typeSubjectRepository.save đã được gọi hay chưa
        verify(typeSubjectMapper, times(1)).toEntity(typeSubjectRequest);
        verify(typeSubjectRepository, times(1)).save(mockTypeSubject);
    }

    @Test
    void editTypeSubject() {
        // Giả lập dữ liệu đầu vào và đối tượng TypeSubject trả về từ repository
        TypeSubject mockTypeSubject = new TypeSubject();
        when(typeSubjectRepository.save(mockTypeSubject)).thenReturn(mockTypeSubject);

        // Gọi phương thức và kiểm tra kết quả
        TypeSubject result = typeSubjectService.editTypeSubject(mockTypeSubject);
        assertEquals(mockTypeSubject, result);

        // Kiểm tra xem typeSubjectRepository.save đã được gọi hay chưa
        verify(typeSubjectRepository, times(1)).save(mockTypeSubject);
    }
}