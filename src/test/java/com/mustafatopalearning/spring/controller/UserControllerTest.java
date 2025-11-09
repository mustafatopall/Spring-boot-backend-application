package com.mustafatopalearning.spring.controller;

import com.mustafatopalearning.spring.dto.UserRequestDTO;
import com.mustafatopalearning.spring.dto.UserResponseDTO;
import com.mustafatopalearning.spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO userResponseDTO;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setEmail("test@example.com");
        userResponseDTO.setName("Test");
        userResponseDTO.setSurname("User");
        userResponseDTO.setCreatedAt(LocalDateTime.now());

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setName("Test");
        userRequestDTO.setSurname("User");
    }

    @Test
    void testGetAllUsers() {
        // Given
        List<UserResponseDTO> users = Arrays.asList(userResponseDTO);
        when(userService.getAllUsers()).thenReturn(users);

        // When
        ResponseEntity<?> response = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        // Given
        when(userService.getUserById(1L)).thenReturn(userResponseDTO);

        // When
        ResponseEntity<?> response = userController.getUserById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testCreateUser() {
        // Given
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // When
        ResponseEntity<?> response = userController.createUser(userRequestDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).createUser(any(UserRequestDTO.class));
    }

    @Test
    void testUpdateUser() {
        // Given
        when(userService.updateUser(eq(1L), any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // When
        ResponseEntity<?> response = userController.updateUser(1L, userRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).updateUser(eq(1L), any(UserRequestDTO.class));
    }

    @Test
    void testDeleteUser() {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When
        ResponseEntity<?> response = userController.deleteUser(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }
}

