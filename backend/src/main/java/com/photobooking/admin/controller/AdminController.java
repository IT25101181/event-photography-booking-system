package com.photobooking.admin.controller;

import com.photobooking.admin.dto.AdminRequestDto;
import com.photobooking.admin.dto.AdminResponseDto;
import com.photobooking.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponseDto> createAdmin(@Valid @RequestBody AdminRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(dto));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AdminResponseDto>> getActiveAdmins() {
        return ResponseEntity.ok(adminService.getActiveAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDto> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDto> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminRequestDto dto) {
        return ResponseEntity.ok(adminService.updateAdmin(id, dto));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleAdminStatus(@PathVariable Long id) {
        adminService.toggleAdminStatus(id);
        return ResponseEntity.ok("Admin status toggled successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
