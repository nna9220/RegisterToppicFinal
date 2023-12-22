package com.web.config;

import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckRoleTest {
    @Mock
    private HttpSession session;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CheckRole checkRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getRoleCurrent_ValidToken_ReturnPerson() {
        // Arrange
        String token = "validToken";
        when(session.getAttribute("token")).thenReturn(token);

        Claims mockClaims = mock(Claims.class);
        when(JwtUtils.extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27")).thenReturn(mockClaims);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userUtils.loadUserByUsername(mockClaims.getSubject())).thenReturn(mockUserDetails);

        Person mockPerson = new Person();
        when(personRepository.findUserByEmail(mockUserDetails.getUsername())).thenReturn(mockPerson);

        // Act
        Person result = CheckRole.getRoleCurrent(session, userUtils, personRepository);

        // Assert
        assertNotNull(result);
        assertSame(mockPerson, result);

        // Verify
        verify(session, times(1)).getAttribute("token");
        //verify(JwtUtils, times(1)).extractClaims(token, "f2f1035db6a255e7885838b020f370d702d4bb0f35a368f06ded1ce8e6684a27");
        verify(userUtils, times(1)).loadUserByUsername(mockClaims.getSubject());
        verify(personRepository, times(1)).findUserByEmail(mockUserDetails.getUsername());
    }

}