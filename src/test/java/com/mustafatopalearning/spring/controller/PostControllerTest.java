package com.mustafatopalearning.spring.controller;

import com.mustafatopalearning.spring.dto.PostRequestDTO;
import com.mustafatopalearning.spring.dto.PostResponseDTO;
import com.mustafatopalearning.spring.service.PostService;
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
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private PostResponseDTO postResponseDTO;
    private PostRequestDTO postRequestDTO;

    @BeforeEach
    void setUp() {
        postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(1L);
        postResponseDTO.setTitle("Test Post");
        postResponseDTO.setContent("Test Content");
        postResponseDTO.setUserId(1L);
        postResponseDTO.setUserName("Test User");
        postResponseDTO.setCreatedAt(LocalDateTime.now());
        postResponseDTO.setUpdatedAt(LocalDateTime.now());

        postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle("Test Post");
        postRequestDTO.setContent("Test Content");
        postRequestDTO.setUserId(1L);
    }

    @Test
    void testGetAllPosts() {
        // Given
        List<PostResponseDTO> posts = Arrays.asList(postResponseDTO);
        when(postService.getAllPosts()).thenReturn(posts);

        // When
        ResponseEntity<?> response = postController.getAllPosts();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testGetPostById() {
        // Given
        when(postService.getPostById(1L)).thenReturn(postResponseDTO);

        // When
        ResponseEntity<?> response = postController.getPostById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    void testGetPostsByUserId() {
        // Given
        List<PostResponseDTO> posts = Arrays.asList(postResponseDTO);
        when(postService.getPostsByUserId(1L)).thenReturn(posts);

        // When
        ResponseEntity<?> response = postController.getPostsByUserId(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).getPostsByUserId(1L);
    }

    @Test
    void testCreatePost() {
        // Given
        when(postService.createPost(any(PostRequestDTO.class))).thenReturn(postResponseDTO);

        // When
        ResponseEntity<?> response = postController.createPost(postRequestDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).createPost(any(PostRequestDTO.class));
    }

    @Test
    void testUpdatePost() {
        // Given
        when(postService.updatePost(eq(1L), any(PostRequestDTO.class))).thenReturn(postResponseDTO);

        // When
        ResponseEntity<?> response = postController.updatePost(1L, postRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).updatePost(eq(1L), any(PostRequestDTO.class));
    }

    @Test
    void testDeletePost() {
        // Given
        doNothing().when(postService).deletePost(1L);

        // When
        ResponseEntity<?> response = postController.deletePost(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(postService, times(1)).deletePost(1L);
    }

    @Test
    void testSearchPosts() {
        // Given
        List<PostResponseDTO> posts = Arrays.asList(postResponseDTO);
        when(postService.searchPosts("test")).thenReturn(posts);

        // When
        ResponseEntity<?> response = postController.searchPosts("test");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(postService, times(1)).searchPosts("test");
    }
}

