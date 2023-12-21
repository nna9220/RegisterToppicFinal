package com.web.service;

import com.web.dto.TokenDto;
import com.web.entity.Person;
import com.web.exception.MessageException;
import com.web.jwt.JwtTokenProvider;
import com.web.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    void login_ValidCredentials_ReturnTokenDto() throws Exception {
        // Arrange
        String username = TEST_USERNAME;
        String password = TEST_PASSWORD;
        Person mockUser = new Person();
        mockUser.setPassword(ENCODED_PASSWORD);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.generateToken(any())).thenReturn("testToken");

        // Act
        TokenDto tokenDto = userService.login(username, password);

        // Assert
        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getToken());
        assertNotNull(tokenDto.getUser());
        verify(jwtTokenProvider, times(1)).generateToken(any());
    }

    @Test
    void login_InvalidCredentials_ThrowsMessageException() {
        // Arrange
        String username = TEST_USERNAME;
        String password = "invalidPassword";
        Person mockUser = new Person();
        mockUser.setPassword(ENCODED_PASSWORD);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(MessageException.class, () -> userService.login(username, password));
        verify(jwtTokenProvider, times(0)).generateToken(any());
    }

    @Test
    void checkUser_UserNotPresent_ThrowsMessageException() {
        // Arrange
        Optional<Person> emptyUser = Optional.empty();

        // Act & Assert
        assertThrows(MessageException.class, () -> userService.checkUser(emptyUser));
    }

    @Test
    void checkUser_UserNotActivated_ThrowsMessageException() {
        // Arrange
        Person inactiveUser = new Person();
        inactiveUser.setActived(false);

        // Act & Assert
        assertThrows(MessageException.class, () -> userService.checkUser(Optional.of(inactiveUser)));
    }

    @Test
    void checkUser_ValidUser_ReturnsTrue() {
        // Arrange
        Person validUser = new Person();
        validUser.setActived(true);

        // Act & Assert
        assertTrue(userService.checkUser(Optional.of(validUser)));
    }

    // Add other UserService test cases as needed
}