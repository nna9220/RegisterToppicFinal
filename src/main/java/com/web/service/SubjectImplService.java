package com.web.service;

import com.web.config.CheckRole;
import com.web.entity.Lecturer;
import com.web.entity.Person;
import com.web.entity.Subject;
import com.web.entity.TypeSubject;
import com.web.repository.*;
import com.web.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectImplService {

    private final SubjectRepository subjectRepository;
    private final TypeSubjectRepository typeSubjectRepository;
    private final PersonRepository personRepository;
    private final UserUtils userUtils;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    public ResponseEntity<?> importSubject(MultipartFile file, HttpSession session) throws IOException {
        try {
            Person current = CheckRole.getRoleCurrent(session, userUtils, personRepository);
            Lecturer lecturer = lecturerRepository.findById(current.getPersonId()).orElse(null);
            TypeSubject typeSubject = typeSubjectRepository.findById(1).orElse(null);
            System.out.println("GV " + lecturer);
            LocalDate nowYear = LocalDate.now();
            if (checkExcelFormat(file)) {
                List<Subject> subjects = toSubjects(file.getInputStream());
                subjects.forEach(subject -> {
                    Subject newSubject =new Subject();
                    newSubject.setSubjectName(subject.getSubjectName());
                    newSubject.setYear(String.valueOf(nowYear));
                    newSubject.setTypeSubject(typeSubject);
                    newSubject.setStatus(false);
                    newSubject.setRequirement(subject.getRequirement());
                    newSubject.setScore(subject.getScore());
                    newSubject.setStudent1(subject.getStudent1());
                    newSubject.setStudentId1(studentRepository.findById(subject.getStudent1()).orElse(null));
                    newSubject.setStudent1(subject.getStudent2());
                    newSubject.setStudentId2(studentRepository.findById(subject.getStudent2()).orElse(null));
                    newSubject.setInstructorId(lecturer);
                    newSubject.setThesisAdvisorId(subject.getThesisAdvisorId());
                    newSubject.setReview(subject.getReview());
                    newSubject.setExpected(subject.getExpected());
                    subjectRepository.save(newSubject);
                    System.out.println("Luu Thanh Cong");
                });
                return ResponseEntity.ok("Imported file to list training program!");
            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload is not match format, please try again!");
        }catch (Exception e){
            throw new RuntimeException("Lỗi r" + e.getMessage());
        }
    }


    public boolean checkExcelFormat(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType == null) throw new AssertionError();
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public List<Subject> toSubjects(InputStream inputStream){
        List<Subject> subjects = new ArrayList<>();
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("subject");
            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int cid = 0;
                Subject subject = new Subject();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cid) {
                        case 0 -> subject.setSubjectName(cell.getStringCellValue());
                        case 1 -> {
                            if (cell == null) {
                                subject.setScore(null); // Or set it to an appropriate default value
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                subject.setScore(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.BLANK) {
                                subject.setScore(null); // Or set it to an appropriate default value
                            } else {
                                throw new IllegalStateException("Unsupported cell type at column 1, row " + row.getRowNum());
                            }
                        }
                        case 2 -> subject.setReview(cell.getStringCellValue());
                        case 3 -> subject.setRequirement(cell.getStringCellValue());
                        case 4 -> subject.setExpected(cell.getStringCellValue());
                        case 5 -> subject.setThesisAdvisorId(lecturerRepository.findById(cell.getStringCellValue()).orElse(null));
                        case 6 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                subject.setStudent1(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                // Xử lý giá trị số nguyên thành chuỗi
                                int numericValue = (int) cell.getNumericCellValue();
                                String stringValue = String.valueOf(numericValue);
                                subject.setStudent1(stringValue);
                            } else if (cell.getCellType() == CellType.BLANK) {
                                subject.setStudent1(null); // Hoặc đặt giá trị mặc định phù hợp
                            } else {
                                // Xử lý các loại ô khác không được hỗ trợ hoặc ném ra ngoại lệ
                                throw new IllegalStateException("Unsupported cell type at column 6, row " + row.getRowNum() +
                                        ". Cell type: " + cell.getCellType());
                            }
                        }

// Similar adjustments for student_2
                        case 7 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                subject.setStudent2(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                // Xử lý giá trị số nguyên thành chuỗi
                                int numericValue = (int) cell.getNumericCellValue();
                                String stringValue = String.valueOf(numericValue);
                                subject.setStudent2(stringValue);
                            } else if (cell.getCellType() == CellType.BLANK) {
                                subject.setStudent2(null); // Hoặc đặt giá trị mặc định phù hợp
                            } else {
                                // Xử lý các loại ô khác không được hỗ trợ hoặc ném ra ngoại lệ
                                throw new IllegalStateException("Unsupported cell type at column 6, row " + row.getRowNum() +
                                        ". Cell type: " + cell.getCellType());
                            }
                        }
                        default -> {
                            throw new IllegalStateException("Unsupported cell type at column " + cid + ", row " + row.getRowNum() +
                                    ". Cell type: " + cell.getCellType());
                        }
                    }
                    cid++;
                }
                subjects.add(subject);
            }


        } catch (Exception e){
            throw new RuntimeException("Error when convert file csv!" + e.getMessage());
        }
        return subjects;
    }


}
