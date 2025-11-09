package com.mustafatopalearning.spring.controller;

import com.mustafatopalearning.spring.dto.ApiResponse;
import com.mustafatopalearning.spring.dto.PageableResponse;
import com.mustafatopalearning.spring.dto.UserRequestDTO;
import com.mustafatopalearning.spring.dto.UserResponseDTO;
import com.mustafatopalearning.spring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Kullanıcı yönetimi API endpoints")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @Operation(summary = "Tüm kullanıcıları listele", description = "Tüm kullanıcıları getirir (pagination olmadan)")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/page")
    @Operation(summary = "Sayfalanmış kullanıcı listesi", description = "Pagination ile kullanıcıları getirir")
    public ResponseEntity<ApiResponse<PageableResponse<UserResponseDTO>>> getAllUsersPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageableResponse<UserResponseDTO> users = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı getir", description = "Belirtilen ID'ye sahip kullanıcıyı getirir")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PostMapping
    @Operation(summary = "Yeni kullanıcı oluştur", description = "Yeni bir kullanıcı oluşturur")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Kullanıcı başarıyla oluşturuldu", createdUser));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Kullanıcı güncelle", description = "Belirtilen ID'ye sahip kullanıcıyı günceller")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı başarıyla güncellendi", updatedUser));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Kullanıcı sil", description = "Belirtilen ID'ye sahip kullanıcıyı siler")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı başarıyla silindi", null));
    }
}

