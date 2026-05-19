package com.photobooking.mediastaff.controller;

import com.photobooking.mediastaff.dto.MediaStaffRequestDto;
import com.photobooking.mediastaff.dto.MediaStaffResponseDto;
import com.photobooking.mediastaff.service.MediaStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media-staff")
@RequiredArgsConstructor
public class MediaStaffController {

    private final MediaStaffService mediaStaffService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<MediaStaffResponseDto> addStaff(@Valid @RequestBody MediaStaffRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaStaffService.addStaff(dto));
    }

    @GetMapping
    public ResponseEntity<List<MediaStaffResponseDto>> getAllStaff() {
        return ResponseEntity.ok(mediaStaffService.getAllStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaStaffResponseDto> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaStaffService.getStaffById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MediaStaffResponseDto>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(mediaStaffService.getByType(type));
    }

    @GetMapping("/available")
    public ResponseEntity<List<MediaStaffResponseDto>> getAvailableStaff() {
        return ResponseEntity.ok(mediaStaffService.getAvailableStaff());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<MediaStaffResponseDto> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody MediaStaffRequestDto dto) {
        return ResponseEntity.ok(mediaStaffService.updateStaff(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> removeStaff(@PathVariable Long id) {
        mediaStaffService.removeStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> toggleAvailability(@PathVariable Long id) {
        mediaStaffService.toggleAvailability(id);
        return ResponseEntity.ok("Availability toggled successfully");
    }
}
