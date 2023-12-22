package com.web.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extractClaims_ValidToken_ReturnClaims() {
        // Arrange
        String jwtToken = "validToken";
        String secretKey = "secretKey";

        // Mocking
        Claims mockClaims = mock(Claims.class);
        when(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody()).thenReturn(mockClaims);

        // Act
        Claims result = JwtUtils.extractClaims(jwtToken, secretKey);

        // Assert
        assertNotNull(result);
        assertSame(mockClaims, result);

        // Verify
        verify(Jwts.parser(), times(1)).setSigningKey(secretKey);
        verify(mockClaims, times(1)).getSubject();
    }

    @Test
    void extractClaims_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalidToken";
        String secretKey = "secretKey";

        // Mocking
        when(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(invalidToken)).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> JwtUtils.extractClaims(invalidToken, secretKey));

        // Verify
        verify(Jwts.parser(), times(1)).setSigningKey(secretKey);
    }

}