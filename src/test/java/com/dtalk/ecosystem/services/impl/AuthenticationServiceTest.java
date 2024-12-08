package com.dtalk.ecosystem.services.impl;

import com.dtalk.ecosystem.DTOs.request.authentication.ChangePasswordRequest;
import com.dtalk.ecosystem.DTOs.request.authentication.SigninRequest;
import com.dtalk.ecosystem.entities.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignin_ValidCredentials() {
        // Given
        SigninRequest request = new SigninRequest();
        request.setEmail("hassen.mrak@example.com");
        request.setPassword("password");

        User mockUser = new User();
        mockUser.setEmail("hassen.mrak@example.com");
        mockUser.setPassword(passwordEncoder.encode("password"));

        // Mock UserRepository behavior
        when(userRepository.findByEmail("hassen.mrak@example.com")).thenReturn(Optional.of(mockUser));

        // Mock JwtService behavior
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        // Mock AuthenticationManager behavior
        // Use when().thenReturn() to specify return value
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // You can return whatever you need here for successful authentication

        // When
        JwtAuthenticationResponse response = authService.signin(request);

        // Then
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());

        verify(userRepository, times(1)).findByEmail("hassen.mrak@example.com");
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testSignin_InvalidCredentials() {
        // Given
        SigninRequest request = new SigninRequest();
        request.setEmail("hassen.mrak@example.com");
        request.setPassword("wrong-password");

        // Mock AuthenticationManager behavior to throw an exception
        doThrow(new IllegalArgumentException("Invalid email or password.")).when(authenticationManager)
                .authenticate(any());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.signin(request);
        });

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    public void testVerifCode_ValidCode() {
        // Given
        String code = "valid-code";
        User mockUser = new User();
        mockUser.setLocked(false);
        when(userRepository.findByVerificationCode(code)).thenReturn(mockUser);

        // When
        boolean result = authService.verifCode(code);

        // Then
        assertTrue(result);
        assertTrue(mockUser.getLocked());

        verify(userRepository, times(1)).findByVerificationCode(code);
        verify(userRepository, times(1)).save(mockUser);
    }
    @Test
    public void testEnableUser() {
        // Given
        long userId = 1L;
        User mockUser = new User();
        mockUser.setIdUser(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        JwtAuthenticationResponse response = authService.enableUser(userId);

        // Then
        assertNotNull(response);
        assertTrue(mockUser.getEnable());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
        verify(jwtService, times(1)).generateToken(mockUser);
    }
    @Test
    public void testDisableUser() {
        // Given
        long userId = 1L;
        User mockUser = new User();
        mockUser.setIdUser(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        JwtAuthenticationResponse response = authService.disableUser(userId);

        // Then
        assertNotNull(response);
        assertFalse(mockUser.getEnable());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
        verify(jwtService, never()).generateToken(any(User.class)); // Vérifie qu'aucun token n'est généré lors de la désactivation
    }

    @Test
    public void testChangePassword_Success() {
        Long userId = 1L;
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("currentPassword");
        req.setNewPassword("newPassword");
        User user = new User();
        user.setPassword("encodedCurrentPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(req.getNewPassword())).thenReturn("encodedNewPassword");

        authService.changePassword(userId,req);

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

}
