package com.assessmentprod.authService;

import com.assessmentprod.authService.entity.UserData;
import com.assessmentprod.authService.entity.role;
import com.assessmentprod.authService.exception.UserAlreadyExistsException;
import com.assessmentprod.authService.repository.UserDataRepository;
import com.assessmentprod.authService.service.AuthService;
import com.assessmentprod.authService.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private UserData mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new UserData();
        mockUser.setUsername("testuser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole(role.USER);
    }

    @Test
    void createUser_WhenUserDoesNotExist_ShouldRegisterUser() {
        when(userDataRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());
        when(userDataRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(mockUser.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<String> response = authService.createUser(mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USER REGISTERED!!", response.getBody());
        verify(userDataRepository).save(mockUser);
    }

    @Test
    void createUser_WhenUserExistsWithUsername_ShouldThrowUserAlreadyExistsException() {
        when(userDataRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> authService.createUser(mockUser));

        assertEquals("User already exists with Username : " + mockUser.getUsername(), exception.getMessage());
    }


    @Test
    void createUser_WhenUserExistsWithEmail_ShouldThrowUserAlreadyExistsException() {
        when(userDataRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());
        when(userDataRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> authService.createUser(mockUser));

        assertEquals("User already exists with Email : " + mockUser.getEmail(), exception.getMessage());
    }


    @Test
    void generateToken_WhenUserExists_ShouldReturnToken() {
        when(userDataRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser.getUsername(), List.of("USER"))).thenReturn("jwtToken");

        String token = authService.generateToken(mockUser.getUsername());

        assertEquals("jwtToken", token);
    }

    @Test
    void generateToken_WhenUserDoesNotExist_ShouldThrowRuntimeException() {
        when(userDataRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.generateToken(mockUser.getUsername()));

        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void validateToken_ShouldReturnTrueWhenTokenIsValid() {
        when(jwtService.isTokenValid("jwtToken")).thenReturn(true);

        boolean isValid = authService.validateToken("jwtToken");

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseWhenTokenIsInvalid() {
        when(jwtService.isTokenValid("jwtToken")).thenReturn(false);

        boolean isValid = authService.validateToken("jwtToken");

        assertFalse(isValid);
    }
}
