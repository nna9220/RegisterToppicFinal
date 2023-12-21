package com.web.jwt;

import com.web.dto.CustomUserDetails;
import com.web.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void doFilter_ValidToken_ShouldSetAuthentication() throws Exception {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
       // CustomUserDetails userDetails = new CustomUserDetails(); // Customize userDetails as needed

        when(jwtTokenProvider.validateToken("validToken")).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromJWT("validToken")).thenReturn("user@example.com");
        //when(personRepository.findByUsername("user@example.com")).thenReturn(userDetails.getUser());

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtTokenProvider).getAuthentication("validToken");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_InvalidToken_ShouldNotSetAuthentication() throws Exception {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtTokenProvider.validateToken("invalidToken")).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtTokenProvider, never()).getAuthentication("invalidToken");
        verify(filterChain).doFilter(request, response);
    }

}