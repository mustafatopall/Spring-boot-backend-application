package com.mustafatopalearning.spring.service;

import com.mustafatopalearning.spring.dto.UserRequestDTO;
import com.mustafatopalearning.spring.dto.UserResponseDTO;
import com.mustafatopalearning.spring.entity.User;
import com.mustafatopalearning.spring.exception.BadRequestException;
import com.mustafatopalearning.spring.exception.ResourceNotFoundException;
import com.mustafatopalearning.spring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setCreatedAt(LocalDateTime.now());

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setName("Test");
        userRequestDTO.setSurname("User");
    }

    @Test
    void testGetAllUsers() {
        // Given
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserResponseDTO result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser_Success() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponseDTO result = userService.createUser(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailExists() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        assertThrows(BadRequestException.class, () -> userService.createUser(userRequestDTO));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setEmail("updated@example.com");
        updateDTO.setName("Updated");
        updateDTO.setSurname("Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponseDTO result = userService.updateUser(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, userRequestDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}

