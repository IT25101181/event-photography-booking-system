package com.photobooking.admin.service;

import com.photobooking.admin.dto.AdminRequestDto;
import com.photobooking.admin.dto.AdminResponseDto;
import com.photobooking.admin.entity.AdminRole;
import com.photobooking.admin.entity.AdminUser;
import com.photobooking.admin.exception.AdminNotFoundException;
import com.photobooking.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServicelmpl implements AdminService{
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDto createAdmin(AdminRequestDto dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already in use: " + dto.getEmail());
        }

        AdminRole role = (dto.getRole() != null && !dto.getRole().isBlank())
                ? AdminRole.valueOf(dto.getRole().toUpperCase())
                : AdminRole.ADMIN;

        AdminUser admin = AdminUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        return mapToDto(adminRepository.save(admin));
    }

    @Override
    public AdminResponseDto getAdminById(Long id) {
        return mapToDto(adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(id)));
    }

    @Override
    public List<AdminResponseDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminResponseDto> getActiveAdmins() {
        return adminRepository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponseDto updateAdmin(Long id, AdminRequestDto dto) {
        AdminUser admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(id));

        admin.setFullName(dto.getFullName());
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            admin.setRole(AdminRole.valueOf(dto.getRole().toUpperCase()));
        }

        return mapToDto(adminRepository.save(admin));
    }

    @Override
    public void toggleAdminStatus(Long id) {
        AdminUser admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(id));
        admin.setActive(!admin.isActive());
        adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new AdminNotFoundException(id);
        }
        adminRepository.deleteById(id); // hard delete
    }

    @Override
    public AdminResponseDto getAdminByEmail(String email) {
        return mapToDto(adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with email: " + email)));
    }

    private AdminResponseDto mapToDto(AdminUser admin) {
        return AdminResponseDto.builder()
                .id(admin.getId())
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role(admin.getRole().name())
                .isActive(admin.isActive())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}
