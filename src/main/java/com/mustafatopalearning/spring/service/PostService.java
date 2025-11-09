package com.mustafatopalearning.spring.service;

import com.mustafatopalearning.spring.dto.PageableResponse;
import com.mustafatopalearning.spring.dto.PostRequestDTO;
import com.mustafatopalearning.spring.dto.PostResponseDTO;
import com.mustafatopalearning.spring.entity.Post;
import com.mustafatopalearning.spring.entity.User;
import com.mustafatopalearning.spring.exception.BadRequestException;
import com.mustafatopalearning.spring.exception.ResourceNotFoundException;
import com.mustafatopalearning.spring.repository.PostRepository;
import com.mustafatopalearning.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    public List<PostResponseDTO> getAllPosts() {
        logger.debug("Tüm postlar getiriliyor");
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PageableResponse<PostResponseDTO> getAllPosts(int page, int size, String sortBy, String sortDir) {
        logger.debug("Sayfalanmış postlar getiriliyor - sayfa: {}, boyut: {}, sıralama: {}", page, size, sortBy);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        
        List<PostResponseDTO> postDTOs = postPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageableResponse.of(postDTOs, page, size, postPage.getTotalElements());
    }
    
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı: " + id));
        return convertToDTO(post);
    }
    
    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        logger.debug("Kullanıcının postları getiriliyor: ID {}", userId);
        
        if (!userRepository.existsById(userId)) {
            logger.error("Kullanıcı bulunamadı: ID {}", userId);
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId);
        }
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PageableResponse<PostResponseDTO> getPostsByUserId(Long userId, int page, int size, String sortBy, String sortDir) {
        logger.debug("Kullanıcının sayfalanmış postları getiriliyor: ID {}, sayfa: {}", userId, page);
        
        if (!userRepository.existsById(userId)) {
            logger.error("Kullanıcı bulunamadı: ID {}", userId);
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId);
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findByUserId(userId, pageable);
        
        List<PostResponseDTO> postDTOs = postPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageableResponse.of(postDTOs, page, size, postPage.getTotalElements());
    }
    
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        logger.info("Yeni post oluşturuluyor: {}", postRequestDTO.getTitle());
        
        User user = userRepository.findById(postRequestDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("Kullanıcı bulunamadı: ID {}", postRequestDTO.getUserId());
                    return new ResourceNotFoundException("Kullanıcı bulunamadı: " + postRequestDTO.getUserId());
                });
        
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setUser(user);
        
        Post savedPost = postRepository.save(post);
        logger.info("Post başarıyla oluşturuldu: ID {}", savedPost.getId());
        return convertToDTO(savedPost);
    }
    
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        logger.info("Post güncelleniyor: ID {}", id);
        
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Post bulunamadı: ID {}", id);
                    return new ResourceNotFoundException("Post bulunamadı: " + id);
                });
        
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        
        Post updatedPost = postRepository.save(post);
        logger.info("Post başarıyla güncellendi: ID {}", updatedPost.getId());
        return convertToDTO(updatedPost);
    }
    
    public void deletePost(Long id) {
        logger.info("Post siliniyor: ID {}", id);
        
        if (!postRepository.existsById(id)) {
            logger.error("Post bulunamadı: ID {}", id);
            throw new ResourceNotFoundException("Post bulunamadı: " + id);
        }
        postRepository.deleteById(id);
        logger.info("Post başarıyla silindi: ID {}", id);
    }
    
    public List<PostResponseDTO> searchPosts(String keyword) {
        logger.debug("Postlar aranıyor: keyword = {}", keyword);
        return postRepository.searchPosts(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PageableResponse<PostResponseDTO> searchPosts(String keyword, int page, int size, String sortBy, String sortDir) {
        logger.debug("Sayfalanmış post araması: keyword = {}, sayfa = {}", keyword, page);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.searchPostsWithPagination(keyword, pageable);
        
        List<PostResponseDTO> postDTOs = postPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageableResponse.of(postDTOs, page, size, postPage.getTotalElements());
    }
    
    private PostResponseDTO convertToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser().getId());
        dto.setUserName(post.getUser().getName() + " " + post.getUser().getSurname());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}

