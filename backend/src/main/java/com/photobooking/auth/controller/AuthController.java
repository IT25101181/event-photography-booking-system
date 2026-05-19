package com.photobooking.auth.controller;

import com.photobooking.auth.dto.LoginRequestDto;
import com.photobooking.auth.dto.LoginResponseDto;
import com.photobooking.auth.dto.RegisterRequestDto;
import com.photobooking.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<java.util.Map<String, String>> register(@Valid @RequestBody RegisterRequestDto dto) {
        String message = authService.register(dto);
        return ResponseEntity.status(201).body(java.util.Map.of("message", message));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
