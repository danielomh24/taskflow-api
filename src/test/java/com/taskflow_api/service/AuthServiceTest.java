package com.taskflow_api.service;

import com.taskflow_api.dto.AuthResponse;
import com.taskflow_api.dto.LoginRequest;
import com.taskflow_api.dto.RegisterRequest;
import com.taskflow_api.model.User;
import com.taskflow_api.repository.UserRepository;
import com.taskflow_api.security.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @DisplayName("Registro exitoso")
    @Test
    public void testRegister_WhenEmailAndUsernameAreUnique_ShouldSucceed() {
        RegisterRequest registerRequest = new RegisterRequest("uniqueUser", "new@example.com", "password");
        User user = User.builder()
                .username("uniqueUser")
                .email("new@example.com")
                .password(passwordEncoder.encode("password"))
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtils.generateToken(anyString())).thenReturn("token");

        AuthResponse authResponse = authService.register(registerRequest);

        assertEquals("token", authResponse.token());
        assertEquals("uniqueUser", authResponse.username());
        assertEquals("new@example.com", authResponse.email());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Registro fallido por email duplicado")
    @Test
    public void testRegister_WhenEmailExists_ShouldThrowException() {
        RegisterRequest registerRequest = new RegisterRequest("existing@example.com", "uniqueUser", "password");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }

    @DisplayName("Registro fallido por username duplicado")
    @Test
    public void testRegister_WhenUsernameExists_ShouldThrowException() {
        RegisterRequest registerRequest = new RegisterRequest("new@example.com", "existingUser", "password");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }

    @DisplayName("Login exitoso")
    @Test
    public void testLogin_WhenUserExists_ShouldSucceed() {
        LoginRequest loginRequest = new LoginRequest("existingUser", "password");
        User user = User.builder()
                .username("existingUser")
                .email("existing@example.com")
                .password(passwordEncoder.encode("password"))
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(anyString())).thenReturn("token");

        AuthResponse authResponse = authService.login(loginRequest);

        assertEquals("token", authResponse.token());
        assertEquals("existingUser", authResponse.username());
        assertEquals("existing@example.com", authResponse.email());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @DisplayName("Login fallido cuando el usuario no existe")
    @Test
    public void testLogin_WhenUserDoesNotExist_ShouldThrowException() {
        LoginRequest loginRequest = new LoginRequest("nonExistentUser", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequest));
    }
}
