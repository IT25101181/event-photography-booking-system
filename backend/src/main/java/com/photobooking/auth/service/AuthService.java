package com.photobooking.auth.service;

import com.photobooking.auth.dto.LoginRequestDto;
import com.photobooking.auth.dto.LoginResponseDto;
import com.photobooking.auth.dto.RegisterRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    String register(RegisterRequestDto dto);
    LoginResponseDto login(LoginRequestDto dto);
}
