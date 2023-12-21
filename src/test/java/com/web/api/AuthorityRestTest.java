package com.web.api;

import com.web.entity.Authority;
import com.web.repository.AuthorityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorityRestTest {
    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityRest authorityRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll_ShouldReturnListOfAuthorities() {
        // Arrange
        Authority authority1 = new Authority();
        authority1.setName("ROLE_USER");

        Authority authority2 = new Authority();
        authority2.setName("ROLE_ADMIN");

        List<Authority> mockAuthorities = Arrays.asList(authority1, authority2);
        when(authorityRepository.findAll()).thenReturn(mockAuthorities);

        // Act
        List<Authority> authorities = authorityRest.findAll();

        // Assert
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertEquals("ROLE_USER", authorities.get(0).getName());
        assertEquals("ROLE_ADMIN", authorities.get(1).getName());
        verify(authorityRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListIfNoAuthorities() {
        // Arrange
        when(authorityRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Authority> authorities = authorityRest.findAll();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
        verify(authorityRepository, times(1)).findAll();
    }

}