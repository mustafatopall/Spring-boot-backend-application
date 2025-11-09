package com.mustafatopalearning.spring.service;

import com.mustafatopalearning.spring.dto.PostRequestDTO;
import com.mustafatopalearning.spring.dto.PostResponseDTO;
import com.mustafatopalearning.spring.entity.Post;
import com.mustafatopalearning.spring.entity.User;
import com.mustafatopalearning.spring.exception.ResourceNotFoundException;
import com.mustafatopalearning.spring.repository.PostRepository;
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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private Post post;
    private User user;
    private PostRequestDTO postRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");

        post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle("Test Post");
        postRequestDTO.setContent("Test Content");
        postRequestDTO.setUserId(1L);
    }

    @Test
    void testGetAllPosts() {
        // Given
        List<Post> posts = Arrays.asList(post);
        when(postRepository.findAll()).thenReturn(posts);

        // When
        List<PostResponseDTO> result = postService.getAllPosts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getTitle());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testGetPostById_Success() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        PostResponseDTO result = postService.getPostById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Post", result.getTitle());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPostById_NotFound() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePost_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostResponseDTO result = postService.createPost(postRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testCreatePost_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> postService.createPost(postRequestDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testUpdatePost_Success() {
        // Given
        PostRequestDTO updateDTO = new PostRequestDTO();
        updateDTO.setTitle("Updated Post");
        updateDTO.setContent("Updated Content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostResponseDTO result = postService.updatePost(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testUpdatePost_NotFound() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(1L, postRequestDTO));
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testDeletePost_Success() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        // When
        postService.deletePost(1L);

        // Then
        verify(postRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePost_NotFound() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> postService.deletePost(1L));
        verify(postRepository, times(1)).existsById(1L);
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetPostsByUserId_Success() {
        // Given
        List<Post> posts = Arrays.asList(post);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(postRepository.findByUserId(1L)).thenReturn(posts);

        // When
        List<PostResponseDTO> result = postService.getPostsByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetPostsByUserId_UserNotFound() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByUserId(1L));
        verify(userRepository, times(1)).existsById(1L);
        verify(postRepository, never()).findByUserId(anyLong());
    }
}

