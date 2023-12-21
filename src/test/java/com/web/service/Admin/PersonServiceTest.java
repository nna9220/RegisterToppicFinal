package com.web.service.Admin;

import com.web.dto.request.PersonRequest;
import com.web.entity.Person;
import com.web.exception.NotFoundException;
import com.web.mapper.PersonMapper;
import com.web.repository.PersonRepository;
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

class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

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
        List<Person> mockPersons = new ArrayList<>();
        when(personRepository.findAllPerson()).thenReturn(mockPersons);

        // Gọi phương thức và kiểm tra kết quả
        List<Person> result = personService.findAll();
        assertEquals(mockPersons, result);

        // Kiểm tra xem repository.findAllPerson đã được gọi hay chưa
        verify(personRepository, times(1)).findAllPerson();
    }

    @Test
    void savePerson() {
        // Giả lập dữ liệu đầu vào và đối tượng Person trả về từ repository
        PersonRequest personRequest = new PersonRequest();
        Person mockPerson = new Person();
        when(personMapper.toEntity(personRequest)).thenReturn(mockPerson);
        when(personRepository.save(mockPerson)).thenReturn(mockPerson);

        // Gọi phương thức và kiểm tra kết quả
        Person result = personService.savePerson(personRequest);
        assertEquals(mockPerson, result);

        // Kiểm tra xem personMapper.toEntity và personRepository.save đã được gọi hay chưa
        verify(personMapper, times(1)).toEntity(personRequest);
        verify(personRepository, times(1)).save(mockPerson);
    }

    @Test
    void getUserEmail() {
        // Giả lập dữ liệu đầu vào và đối tượng Person trả về từ repository
        String email = "test@example.com";
        Person mockPerson = new Person();
        when(personRepository.findUserByEmail(email)).thenReturn(mockPerson);

        // Gọi phương thức và kiểm tra kết quả
        Person result = personService.getUserEmail(email);
        assertEquals(mockPerson, result);

        // Kiểm tra xem personRepository.findUserByEmail đã được gọi hay chưa
        verify(personRepository, times(1)).findUserByEmail(email);
    }

    @Test
    void editPerson() {
        // Giả lập dữ liệu đầu vào và đối tượng Person trả về từ repository
        String id = "1";
        PersonRequest personRequest = new PersonRequest();
        Person oldPerson = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(oldPerson));
        when(personRepository.save(oldPerson)).thenReturn(oldPerson);

        // Gọi phương thức và kiểm tra kết quả
        Person result = personService.editPerson(id, personRequest);
        assertEquals(oldPerson, result);

        // Kiểm tra xem personRepository.findById và personRepository.save đã được gọi hay chưa
        verify(personRepository, times(1)).findById(id);
        verify(personRepository, times(1)).save(oldPerson);
    }

    @Test
    void editPersonNotFound() {
        // Giả lập dữ liệu đầu vào với id không tồn tại
        String nonExistentId = "100";
        when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        assertThrows(NotFoundException.class, () -> personService.editPerson(nonExistentId, new PersonRequest()));

        // Kiểm tra xem personRepository.findById đã được gọi hay chưa
        verify(personRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void deletePerson() {
        // Giả lập dữ liệu đầu vào và đối tượng Person trả về từ repository
        String id = "1";
        Person oldPerson = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(oldPerson));
        when(personRepository.save(oldPerson)).thenReturn(oldPerson);

        // Gọi phương thức và kiểm tra kết quả
        Person result = personService.deletePerson(id);
        assertEquals(oldPerson, result);

        // Kiểm tra xem personRepository.findById và personRepository.save đã được gọi hay chưa
        verify(personRepository, times(1)).findById(id);
        verify(personRepository, times(1)).save(oldPerson);
    }

    @Test
    void deletePersonNotFound() {
        // Giả lập dữ liệu đầu vào với id không tồn tại
        String nonExistentId = "100";
        when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        assertThrows(NotFoundException.class, () -> personService.deletePerson(nonExistentId));

        // Kiểm tra xem personRepository.findById đã được gọi hay chưa
        verify(personRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void detailPerson() {
        // Giả lập dữ liệu đầu vào và đối tượng Person trả về từ repository
        String id = "1";
        Person mockPerson = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(mockPerson));

        // Gọi phương thức và kiểm tra kết quả
        Person result = personService.detailPerson(id);
        assertEquals(mockPerson, result);

        // Kiểm tra xem personRepository.findById đã được gọi hay chưa
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void detailPersonNotFound() {
        // Giả lập dữ liệu đầu vào với id không tồn tại
        String nonExistentId = "100";
        when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        assertThrows(NotFoundException.class, () -> personService.detailPerson(nonExistentId));

        // Kiểm tra xem personRepository.findById đã được gọi hay chưa
        verify(personRepository, times(1)).findById(nonExistentId);
    }
}