package com.mustafatopalearning.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDTO {
    
    @NotBlank(message = "Başlık alanı boş olamaz")
    @Size(min = 3, max = 200, message = "Başlık 3 ile 200 karakter arasında olmalıdır")
    private String title;
    
    @NotBlank(message = "İçerik alanı boş olamaz")
    @Size(min = 10, message = "İçerik en az 10 karakter olmalıdır")
    private String content;
    
    private Long userId;
}

