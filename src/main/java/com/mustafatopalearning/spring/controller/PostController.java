package com.mustafatopalearning.spring.controller;

import com.mustafatopalearning.spring.dto.ApiResponse;
import com.mustafatopalearning.spring.dto.PageableResponse;
import com.mustafatopalearning.spring.dto.PostRequestDTO;
import com.mustafatopalearning.spring.dto.PostResponseDTO;
import com.mustafatopalearning.spring.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Controller", description = "Post yönetimi API endpoints")
public class PostController {
    
    private final PostService postService;
    
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping
    @Operation(summary = "Tüm postları listele", description = "Tüm postları getirir (pagination olmadan)")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/page")
    @Operation(summary = "Sayfalanmış post listesi", description = "Pagination ile postları getirir")
    public ResponseEntity<ApiResponse<PageableResponse<PostResponseDTO>>> getAllPostsPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PageableResponse<PostResponseDTO> posts = postService.getAllPosts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre post getir", description = "Belirtilen ID'ye sahip postu getirir")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanıcının postlarını listele", description = "Belirtilen kullanıcının tüm postlarını getirir")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPostsByUserId(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/user/{userId}/page")
    @Operation(summary = "Kullanıcının sayfalanmış postları", description = "Pagination ile kullanıcının postlarını getirir")
    public ResponseEntity<ApiResponse<PageableResponse<PostResponseDTO>>> getPostsByUserIdPageable(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PageableResponse<PostResponseDTO> posts = postService.getPostsByUserId(userId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Post ara", description = "Anahtar kelimeye göre post arama yapar")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> searchPosts(@RequestParam String keyword) {
        List<PostResponseDTO> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/search/page")
    @Operation(summary = "Sayfalanmış post arama", description = "Pagination ile post arama yapar")
    public ResponseEntity<ApiResponse<PageableResponse<PostResponseDTO>>> searchPostsPageable(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PageableResponse<PostResponseDTO> posts = postService.searchPosts(keyword, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @PostMapping
    @Operation(summary = "Yeni post oluştur", description = "Yeni bir post oluşturur")
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO createdPost = postService.createPost(postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post başarıyla oluşturuldu", createdPost));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Post güncelle", description = "Belirtilen ID'ye sahip postu günceller")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(
            @PathVariable Long id, 
            @Valid @RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO updatedPost = postService.updatePost(id, postRequestDTO);
        return ResponseEntity.ok(ApiResponse.success("Post başarıyla güncellendi", updatedPost));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Post sil", description = "Belirtilen ID'ye sahip postu siler")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post başarıyla silindi", null));
    }
}

