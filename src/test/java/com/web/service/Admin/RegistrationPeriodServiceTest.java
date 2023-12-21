package com.web.service.Admin;

import com.web.dto.request.RegistrationPeriodRequest;
import com.web.entity.RegistrationPeriod;
import com.web.mapper.RegistrationPeriodMapper;
import com.web.repository.RegistrationPeriodRepository;
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

class RegistrationPeriodServiceTest {
    @Mock
    private RegistrationPeriodRepository registrationPeriodRepository;

    @Mock
    private RegistrationPeriodMapper registrationPeriodMapper;

    @InjectMocks
    private RegistrationPeriodService registrationPeriodService;

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
        List<RegistrationPeriod> mockPeriods = new ArrayList<>();
        when(registrationPeriodRepository.findAllPeriod()).thenReturn(mockPeriods);

        // Gọi phương thức và kiểm tra kết quả
        List<RegistrationPeriod> result = registrationPeriodService.findAll();
        assertEquals(mockPeriods, result);

        // Kiểm tra xem repository.findAllPeriod đã được gọi hay chưa
        verify(registrationPeriodRepository, times(1)).findAllPeriod();
    }

    @Test
    void createPeriod() {
        // Giả lập dữ liệu đầu vào và đối tượng RegistrationPeriod trả về từ repository
        RegistrationPeriodRequest registrationPeriodRequest = new RegistrationPeriodRequest();
        RegistrationPeriod mockPeriod = new RegistrationPeriod();
        when(registrationPeriodMapper.toEntity(registrationPeriodRequest)).thenReturn(mockPeriod);
        when(registrationPeriodRepository.save(mockPeriod)).thenReturn(mockPeriod);

        // Gọi phương thức và kiểm tra kết quả
        RegistrationPeriod result = registrationPeriodService.createPeriod(registrationPeriodRequest);
        assertEquals(mockPeriod, result);

        // Kiểm tra xem registrationPeriodMapper.toEntity và registrationPeriodRepository.save đã được gọi hay chưa
        verify(registrationPeriodMapper, times(1)).toEntity(registrationPeriodRequest);
        verify(registrationPeriodRepository, times(1)).save(mockPeriod);
    }

    @Test
    void editPeriod() {
        // Giả lập dữ liệu đầu vào và đối tượng RegistrationPeriod trả về từ repository
        RegistrationPeriod mockPeriod = new RegistrationPeriod();
        when(registrationPeriodRepository.save(mockPeriod)).thenReturn(mockPeriod);

        // Gọi phương thức và kiểm tra kết quả
        RegistrationPeriod result = registrationPeriodService.editPeriod(mockPeriod);
        assertEquals(mockPeriod, result);

        // Kiểm tra xem registrationPeriodRepository.save đã được gọi hay chưa
        verify(registrationPeriodRepository, times(1)).save(mockPeriod);
    }
}