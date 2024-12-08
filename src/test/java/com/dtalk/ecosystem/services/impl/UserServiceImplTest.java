package com.dtalk.ecosystem.services.impl;

import com.dtalk.ecosystem.entities.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testLoadUserByUsername_UserFound() {
        User mockUser = new User();
        mockUser.setEmail("hassen@test.com");
        when(userRepository.findByEmail("hassen@test.com")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername("hassen@test.com");

        assertEquals("hassen@test.com", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("hassen@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.userDetailsService().loadUserByUsername("hassen@test.com");
        });
    }


    @Test
    public void testGetUserById_UserFound() {
        User mockUser = new User();
        mockUser.setIdUser(1L);  // Le suffixe L indique que 1 est un long
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertEquals(1L, user.getIdUser());
    }

    @Test
    public void testRetrieveAllUserByRole() {
        User mockUser = new User();
        mockUser.setRole(Role.ADMIN);
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(List.of(mockUser));

        List<User> users = userService.retrieveAllUserByRole("ADMIN");

        assertEquals(1, users.size());
        assertEquals(Role.ADMIN, users.get(0).getRole());
    }



}
