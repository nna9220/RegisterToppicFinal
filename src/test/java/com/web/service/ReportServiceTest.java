package com.web.service;

import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.repository.LecturerRepository;
import com.web.repository.PersonRepository;
import com.web.repository.SubjectRepository;
import com.web.utils.UserUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.Subject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReportServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private LecturerRepository lecturerRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateExcel() throws IOException {
        // Giả lập dữ liệu đầu vào
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // Giả lập dữ liệu trả về từ repository và userUtils
        Person mockPerson = new Person();
        when(personRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(mockPerson));
        Lecturer mockLecturer = new Lecturer();
        when(lecturerRepository.findById(anyString())).thenReturn(java.util.Optional.of(mockLecturer));
        List<Subject> mockSubjects = new ArrayList<>();
        //when(subjectRepository.getSubjectByMajor(any())).thenReturn(mockSubjects);

        // Tạo biến ByteArrayOutputStream để lưu dữ liệu xuất Excel
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //when(response.getOutputStream()).thenReturn(servletOutputStream);

        // Gọi phương thức và kiểm tra kết quả
        reportService.generateExcel(response, session);

        // Kiểm tra xem response.getOutputStream đã được gọi hay chưa
        verify(response, times(1)).getOutputStream();

        // Đọc dữ liệu từ ByteArrayOutputStream và kiểm tra nội dung Excel được tạo
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        HSSFWorkbook workbook = new HSSFWorkbook(new ByteArrayInputStream(excelBytes));
        HSSFSheet sheet = workbook.getSheetAt(0);

        // Kiểm tra dòng đầu tiên của sheet (header)
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow);
        assertEquals("subjectId", headerRow.getCell(0).getStringCellValue());
        assertEquals("subjectName", headerRow.getCell(1).getStringCellValue());
        assertEquals("major", headerRow.getCell(2).getStringCellValue());
        assertEquals("student1", headerRow.getCell(3).getStringCellValue());
        assertEquals("student2", headerRow.getCell(4).getStringCellValue());
        assertEquals("instructorId", headerRow.getCell(5).getStringCellValue());

        // Kiểm tra xem workbook.close và ops.close đã được gọi hay chưa
        verify(workbook, times(1)).close();
        //verify(servletOutputStream, times(1)).close();
    }
}