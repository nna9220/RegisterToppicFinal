package com.web.service.Admin;

import com.web.dto.request.SchoolYearRequest;
import com.web.entity.SchoolYear;
import com.web.mapper.SchoolYearMapper;
import com.web.repository.SchoolYearRepository;
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

class SchoolYearServiceTest {
    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private SchoolYearMapper schoolYearMapper;

    @InjectMocks
    private SchoolYearService schoolYearService;

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
        List<SchoolYear> mockYears = new ArrayList<>();
        when(schoolYearRepository.getAllSchoolYear()).thenReturn(mockYears);

        // Gọi phương thức và kiểm tra kết quả
        List<SchoolYear> result = schoolYearService.findAll();
        assertEquals(mockYears, result);

        // Kiểm tra xem repository.getAllSchoolYear đã được gọi hay chưa
        verify(schoolYearRepository, times(1)).getAllSchoolYear();
    }

    @Test
    void createSchoolYear() {
        // Giả lập dữ liệu đầu vào và đối tượng SchoolYear trả về từ repository
        SchoolYearRequest schoolYearRequest = new SchoolYearRequest();
        SchoolYear mockYear = new SchoolYear();
        when(schoolYearMapper.toEntity(schoolYearRequest)).thenReturn(mockYear);
        when(schoolYearRepository.save(mockYear)).thenReturn(mockYear);

        // Gọi phương thức và kiểm tra kết quả
        SchoolYear result = schoolYearService.createSchoolYear(schoolYearRequest);
        assertEquals(mockYear, result);

        // Kiểm tra xem schoolYearMapper.toEntity và schoolYearRepository.save đã được gọi hay chưa
        verify(schoolYearMapper, times(1)).toEntity(schoolYearRequest);
        verify(schoolYearRepository, times(1)).save(mockYear);
    }

    @Test
    void editSchoolYear() {
        // Giả lập dữ liệu đầu vào và đối tượng SchoolYear trả về từ repository
        SchoolYear mockYear = new SchoolYear();
        when(schoolYearRepository.save(mockYear)).thenReturn(mockYear);

        // Gọi phương thức và kiểm tra kết quả
        SchoolYear result = schoolYearService.editSchoolYear(mockYear);
        assertEquals(mockYear, result);

        // Kiểm tra xem schoolYearRepository.save đã được gọi hay chưa
        verify(schoolYearRepository, times(1)).save(mockYear);
    }
}