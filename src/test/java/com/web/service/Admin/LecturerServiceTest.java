package com.web.service.Admin;

import com.web.dto.request.LecturerRequest;
import com.web.entity.Lecturer;
import com.web.exception.NotFoundException;
import com.web.mapper.LecturerMapper;
import com.web.repository.LecturerRepository;
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

class LecturerServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private LecturerMapper lecturerMapper;

    @InjectMocks
    private LecturerService lecturerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllLec() {
        // Giả lập dữ liệu trả về từ repository
        List<Lecturer> mockLecturers = new ArrayList<>();
        when(lecturerRepository.findAllLec()).thenReturn(mockLecturers);

        // Gọi phương thức và kiểm tra kết quả
        List<Lecturer> result = lecturerService.getAllLec();
        assertEquals(mockLecturers, result);

        // Kiểm tra xem repository.findAllLec đã được gọi hay chưa
        verify(lecturerRepository, times(1)).findAllLec();
    }

    @Test
    void saveLecturer() {
        // Giả lập dữ liệu đầu vào và đối tượng Lecturer trả về từ repository
        LecturerRequest lecturerRequest = new LecturerRequest();
        Lecturer mockLecturer = new Lecturer();
        when(lecturerMapper.toEntity(lecturerRequest)).thenReturn(mockLecturer);
        when(lecturerRepository.save(mockLecturer)).thenReturn(mockLecturer);

        // Gọi phương thức và kiểm tra kết quả
        Lecturer result = lecturerService.saveLecturer(lecturerRequest);
        assertEquals(mockLecturer, result);

        // Kiểm tra xem mapper.toEntity và repository.save đã được gọi hay chưa
        verify(lecturerMapper, times(1)).toEntity(lecturerRequest);
        verify(lecturerRepository, times(1)).save(mockLecturer);
    }

    @Test
    void detail() {
        // Giả lập dữ liệu đầu vào và đối tượng Lecturer trả về từ repository
        String lecturerId = "1";
        Lecturer mockLecturer = new Lecturer();
        when(lecturerRepository.findById(lecturerId)).thenReturn(Optional.of(mockLecturer));

        // Gọi phương thức và kiểm tra kết quả
        Lecturer result = lecturerService.detail(lecturerId);
        assertEquals(mockLecturer, result);

        // Kiểm tra xem repository.findById đã được gọi hay chưa
        verify(lecturerRepository, times(1)).findById(lecturerId);
    }

    @Test
    void detailNotFound() {
        // Giả lập dữ liệu đầu vào với lecturerId không tồn tại
        String nonExistentLecturerId = "100";
        when(lecturerRepository.findById(nonExistentLecturerId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        assertThrows(NotFoundException.class, () -> lecturerService.detail(nonExistentLecturerId));

        // Kiểm tra xem repository.findById đã được gọi hay chưa
        verify(lecturerRepository, times(1)).findById(nonExistentLecturerId);
    }
}