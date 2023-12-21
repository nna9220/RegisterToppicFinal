package com.web.api;

import com.web.dto.LoginDto;
import com.web.dto.TokenDto;
import com.web.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserResourceTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserResource userResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void authenticate_ValidCredentials_ReturnTokenDto() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("testPassword");

        TokenDto mockTokenDto = new TokenDto();
        when(userService.login(loginDto.getUsername(), loginDto.getPassword())).thenReturn(mockTokenDto);

        // Act
        TokenDto tokenDto = userResource.authenticate(loginDto);

        // Assert
        assertNotNull(tokenDto);
        assertSame(mockTokenDto, tokenDto);
    }


    @Test
    void checkRoleAdmin() {
    }

    @Test
    void checkRoleUser() {
    }
}