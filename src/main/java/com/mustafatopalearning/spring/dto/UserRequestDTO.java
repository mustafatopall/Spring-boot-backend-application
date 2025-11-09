package com.mustafatopalearning.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    
    @NotBlank(message = "Email alanı boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;
    
    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Ad 2 ile 50 karakter arasında olmalıdır")
    private String name;
    
    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2 ile 50 karakter arasında olmalıdır")
    private String surname;
}

