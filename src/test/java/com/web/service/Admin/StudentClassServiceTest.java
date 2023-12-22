package com.web.service.Admin;

import com.web.dto.request.StudentClassRequest;
import com.web.entity.StudentClass;
import com.web.mapper.StudentClassMapper;
import com.web.repository.StudentClassRepository;
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

class StudentClassServiceTest {
    @Mock
    private StudentClassRepository studentClassRepository;

    @Mock
    private StudentClassMapper studentClassMapper;

    @InjectMocks
    private StudentClassService studentClassService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createStudentClass() {
        // Giả lập dữ liệu đầu vào và đối tượng StudentClass trả về từ repository
        StudentClassRequest studentClassRequest = new StudentClassRequest();
        StudentClass mockStudentClass = new StudentClass();
        when(studentClassMapper.toEntity(studentClassRequest)).thenReturn(mockStudentClass);
        when(studentClassRepository.save(mockStudentClass)).thenReturn(mockStudentClass);

        // Gọi phương thức và kiểm tra kết quả
        StudentClass result = studentClassService.createStudentClass(studentClassRequest);
        assertEquals(mockStudentClass, result);

        // Kiểm tra xem studentClassMapper.toEntity và studentClassRepository.save đã được gọi hay chưa
        verify(studentClassMapper, times(1)).toEntity(studentClassRequest);
        verify(studentClassRepository, times(1)).save(mockStudentClass);
    }

    @Test
    void findAll() {
        // Giả lập dữ liệu trả về từ repository
        List<StudentClass> mockStudentClasses = new ArrayList<>();
        when(studentClassRepository.getAllStudentClass()).thenReturn(mockStudentClasses);

        // Gọi phương thức và kiểm tra kết quả
        List<StudentClass> result = studentClassService.findAll();
        assertEquals(mockStudentClasses, result);

        // Kiểm tra xem repository.getAllStudentClass đã được gọi hay chưa
        verify(studentClassRepository, times(1)).getAllStudentClass();
    }

    @Test
    void getStudentClassById() {
        // Giả lập dữ liệu trả về từ repository
        int studentClassId = 1;
        StudentClass mockStudentClass = new StudentClass();
        when(studentClassRepository.findById(studentClassId)).thenReturn(java.util.Optional.of(mockStudentClass));

        // Gọi phương thức và kiểm tra kết quả
        StudentClass result = studentClassService.getStudentClassById(studentClassId);
        assertEquals(mockStudentClass, result);

        // Kiểm tra xem repository.findById đã được gọi hay chưa
        verify(studentClassRepository, times(1)).findById(studentClassId);
    }

    @Test
    void editStudentClass() {
        // Giả lập dữ liệu đầu vào và đối tượng StudentClass trả về từ repository
        StudentClass mockStudentClass = new StudentClass();
        when(studentClassRepository.save(mockStudentClass)).thenReturn(mockStudentClass);

        // Gọi phương thức và kiểm tra kết quả
        StudentClass result = studentClassService.editStudentClass(mockStudentClass);
        assertEquals(mockStudentClass, result);

        // Kiểm tra xem studentClassRepository.save đã được gọi hay chưa
        verify(studentClassRepository, times(1)).save(mockStudentClass);
    }
}