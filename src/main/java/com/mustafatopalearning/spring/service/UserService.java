package com.mustafatopalearning.spring.service;

import com.mustafatopalearning.spring.dto.PageableResponse;
import com.mustafatopalearning.spring.dto.UserRequestDTO;
import com.mustafatopalearning.spring.dto.UserResponseDTO;
import com.mustafatopalearning.spring.entity.User;
import com.mustafatopalearning.spring.exception.BadRequestException;
import com.mustafatopalearning.spring.exception.ResourceNotFoundException;
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
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<UserResponseDTO> getAllUsers() {
        logger.debug("Tüm kullanıcılar getiriliyor");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PageableResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {
        logger.debug("Sayfalanmış kullanıcılar getiriliyor - sayfa: {}, boyut: {}, sıralama: {}", page, size, sortBy);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findAll(pageable);
        
        List<UserResponseDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageableResponse.of(userDTOs, page, size, userPage.getTotalElements());
    }
    
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        return convertToDTO(user);
    }
    
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        logger.info("Yeni kullanıcı oluşturuluyor: {}", userRequestDTO.getEmail());
        
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            logger.warn("Email adresi zaten kullanılıyor: {}", userRequestDTO.getEmail());
            throw new BadRequestException("Bu email adresi zaten kullanılıyor: " + userRequestDTO.getEmail());
        }
        
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        user.setSurname(userRequestDTO.getSurname());
        
        User savedUser = userRepository.save(user);
        logger.info("Kullanıcı başarıyla oluşturuldu: ID {}", savedUser.getId());
        return convertToDTO(savedUser);
    }
    
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        logger.info("Kullanıcı güncelleniyor: ID {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Kullanıcı bulunamadı: ID {}", id);
                    return new ResourceNotFoundException("Kullanıcı bulunamadı: " + id);
                });
        
        if (!user.getEmail().equals(userRequestDTO.getEmail()) && 
            userRepository.existsByEmail(userRequestDTO.getEmail())) {
            logger.warn("Email adresi zaten kullanılıyor: {}", userRequestDTO.getEmail());
            throw new BadRequestException("Bu email adresi zaten kullanılıyor: " + userRequestDTO.getEmail());
        }
        
        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        user.setSurname(userRequestDTO.getSurname());
        
        User updatedUser = userRepository.save(user);
        logger.info("Kullanıcı başarıyla güncellendi: ID {}", updatedUser.getId());
        return convertToDTO(updatedUser);
    }
    
    public void deleteUser(Long id) {
        logger.info("Kullanıcı siliniyor: ID {}", id);
        
        if (!userRepository.existsById(id)) {
            logger.error("Kullanıcı bulunamadı: ID {}", id);
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
        logger.info("Kullanıcı başarıyla silindi: ID {}", id);
    }
    
    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}

