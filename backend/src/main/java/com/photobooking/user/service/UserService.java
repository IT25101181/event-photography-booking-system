package com.photobooking.user.service;

import com.photobooking.user.dto.UserRequestDto;
import com.photobooking.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    void deleteUser(Long id);
    void toggleUserStatus(Long id);
    UserResponseDto getUserByEmail(String email);
}
