package com.web.controller.admin;

import com.web.entity.Authority;
import com.web.entity.Person;
import com.web.repository.PersonRepository;
import com.web.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HomeAdminControllerTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private HomeAdminController homeAdminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getHome() {

    }

    @Test
    void getProfile() {
    }

    @Test
    void getEditProfile() {
    }

    @Test
    void updateLecturer() {
    }
}