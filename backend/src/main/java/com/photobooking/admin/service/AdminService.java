package com.photobooking.admin.service;

import com.photobooking.admin.dto.AdminRequestDto;
import com.photobooking.admin.dto.AdminResponseDto;

import java.util.List;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRequestDto dto);
    AdminResponseDto getAdminById(Long id);
    List<AdminResponseDto> getAllAdmins();
    List<AdminResponseDto> getActiveAdmins();
    AdminResponseDto updateAdmin(Long id, AdminRequestDto dto);
    void toggleAdminStatus(Long id);
    void deleteAdmin(Long id);
    AdminResponseDto getAdminByEmail(String email);
}
