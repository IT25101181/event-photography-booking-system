package com.photobooking.user.service;

import com.photobooking.auth.entity.AppUser;
import com.photobooking.auth.repository.AppUserRepository;
import com.photobooking.user.dto.UserRequestDto;
import com.photobooking.user.dto.UserResponseDto;
import com.photobooking.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already in use: " + dto.getEmail());
        }

        AppUser user = AppUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .role("USER")
                .isActive(true)
                .build();

        AppUser saved = userRepository.save(user);
        return mapToResponseDto(saved);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Check if email is changing and if new email is already in use
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalStateException("Email already in use: " + dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        
        // Update password if provided and long enough
        if (dto.getPassword() != null && !dto.getPassword().isBlank() && 
            !dto.getPassword().equals("placeholder12") && dto.getPassword().length() >= 6) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        AppUser updated = userRepository.save(user);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void toggleUserStatus(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToResponseDto(user);
    }

    private UserResponseDto mapToResponseDto(AppUser user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
