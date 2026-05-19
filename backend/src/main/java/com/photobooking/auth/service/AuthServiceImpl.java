package com.photobooking.auth.service;

import com.photobooking.auth.dto.LoginRequestDto;
import com.photobooking.auth.dto.LoginResponseDto;
import com.photobooking.auth.dto.RegisterRequestDto;
import com.photobooking.auth.entity.AppUser;
import com.photobooking.auth.exception.InvalidCredentialsException;
import com.photobooking.auth.repository.AppUserRepository;
import com.photobooking.admin.repository.AdminRepository;
import com.photobooking.admin.entity.AdminUser;
import com.photobooking.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequestDto dto) {
        if (appUserRepository.existsByEmail(dto.getEmail()) || adminRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered: " + dto.getEmail());
        }

        String role = "USER";

        AppUser user = AppUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        appUserRepository.save(user);
        return "User registered successfully with email: " + dto.getEmail();
    }


    private LoginResponseDto loginAsAdmin(LoginRequestDto dto) {
        AdminUser admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!admin.isActive()) {
            throw new InvalidCredentialsException("Account is deactivated");
        }

        String token = jwtUtil.generateToken(admin);

        return LoginResponseDto.builder()
                .id(admin.getId())
                .token(token)
                .email(admin.getEmail())
                .role(admin.getRole().name())
                .fullName(admin.getFullName())
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        try {
            AppUser user = appUserRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("User not found"));

            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Invalid email or password");
            }

            if (!user.isActive()) {
                throw new InvalidCredentialsException("Account is deactivated");
            }

            String token = jwtUtil.generateToken(user);

            return LoginResponseDto.builder()
                    .id(user.getId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .fullName(user.getFullName())
                    .build();
        } catch (InvalidCredentialsException e) {
            // If not found in AppUser, try AdminUser
            return loginAsAdmin(dto);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check AppUser
        var appUser = appUserRepository.findByEmail(email);
        if (appUser.isPresent()) return appUser.get();

        // Check AdminUser
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
