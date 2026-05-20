package com.photobooking.mediastaff.service;

import com.photobooking.mediastaff.dto.MediaStaffRequestDto;
import com.photobooking.mediastaff.dto.MediaStaffResponseDto;

import java.util.List;

public interface MediaStaffService {

    MediaStaffResponseDto addStaff(MediaStaffRequestDto dto);

    MediaStaffResponseDto getStaffById(Long id);

    List<MediaStaffResponseDto> getAllStaff();

    List<MediaStaffResponseDto> getByType(String type);

    List<MediaStaffResponseDto> getAvailableStaff();

    MediaStaffResponseDto updateStaff(Long id, MediaStaffRequestDto dto);

    void removeStaff(Long id);

    void toggleAvailability(Long id);
}
