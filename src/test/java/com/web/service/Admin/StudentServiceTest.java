package com.web.service.Admin;

import com.web.dto.request.StudentRequest;
import com.web.entity.Student;
import com.web.exception.NotFoundException;
import com.web.mapper.StudentMapper;
import com.web.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllStudent() {
        // Giả lập dữ liệu trả về từ repository
        List<Student> mockStudents = new ArrayList<>();
        when(studentRepository.getAllStudent()).thenReturn(mockStudents);

        // Gọi phương thức và kiểm tra kết quả
        List<Student> result = studentService.getAllStudent();
        assertEquals(mockStudents, result);

        // Kiểm tra xem repository.getAllStudent đã được gọi hay chưa
        verify(studentRepository, times(1)).getAllStudent();
    }

    @Test
    void saveStudent() {
        // Giả lập dữ liệu đầu vào và đối tượng Student trả về từ repository
        StudentRequest studentRequest = new StudentRequest();
        Student mockStudent = new Student();
        when(studentMapper.toEntity(studentRequest)).thenReturn(mockStudent);
        when(studentRepository.save(mockStudent)).thenReturn(mockStudent);

        // Gọi phương thức và kiểm tra kết quả
        Student result = studentService.saveStudent(studentRequest);
        assertEquals(mockStudent, result);

        // Kiểm tra xem studentMapper.toEntity và studentRepository.save đã được gọi hay chưa
        verify(studentMapper, times(1)).toEntity(studentRequest);
        verify(studentRepository, times(1)).save(mockStudent);
    }

    @Test
    void detail() {
        // Giả lập dữ liệu đầu vào và đối tượng Student trả về từ repository
        String studentId = "1";
        Student mockStudent = new Student();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

        // Gọi phương thức và kiểm tra kết quả
        Student result = studentService.detail(studentId);
        assertEquals(mockStudent, result);

        // Kiểm tra xem repository.findById đã được gọi hay chưa
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void detail_ThrowsNotFoundException() {
        // Giả lập dữ liệu đầu vào và đối tượng Student không tồn tại trong repository
        String studentId = "1";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        assertThrows(NotFoundException.class, () -> studentService.detail(studentId));

        // Kiểm tra xem repository.findById đã được gọi hay chưa
        verify(studentRepository, times(1)).findById(studentId);
    }
}