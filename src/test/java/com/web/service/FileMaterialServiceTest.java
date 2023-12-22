package com.web.service;

import com.web.entity.FileComment;
import com.web.repository.FileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileMaterialServiceTest {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileMaterialService fileMaterialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void storeFile() {
        // Giả lập dữ liệu đầu vào
        MultipartFile mockFile = new MockMultipartFile("test-file.txt", "Hello, World!".getBytes());

        // Gọi phương thức và kiểm tra kết quả
        String result = fileMaterialService.storeFile(mockFile);
        assertNotNull(result);

        // Kiểm tra xem fileRepository.save đã được gọi hay chưa
        verify(fileRepository, times(1)).save(any(FileComment.class));
    }

    @Test
    void loadFileAsResource() {
        // Giả lập dữ liệu đầu vào
        String fileName = "test-file.txt";

        // Giả lập dữ liệu trả về từ repository
        FileComment mockFileComment = new FileComment();
        when(fileRepository.getFileByName(fileName)).thenReturn(mockFileComment);

        // Gọi phương thức và kiểm tra kết quả
        Resource result = fileMaterialService.loadFileAsResource(fileName);
        assertNotNull(result);

        // Kiểm tra xem fileRepository.getFileByName đã được gọi hay chưa
        verify(fileRepository, times(1)).getFileByName(fileName);
    }

    @Test
    void uploadFile() {
        // Giả lập dữ liệu đầu vào
        FileComment mockFileComment = new FileComment();

        // Giả lập dữ liệu trả về từ repository
        when(fileRepository.save(mockFileComment)).thenReturn(mockFileComment);

        // Gọi phương thức và kiểm tra kết quả
        FileComment result = fileMaterialService.uploadFile(mockFileComment);
        assertNotNull(result);

        // Kiểm tra xem fileRepository.save đã được gọi hay chưa
        verify(fileRepository, times(1)).save(mockFileComment);
    }
}