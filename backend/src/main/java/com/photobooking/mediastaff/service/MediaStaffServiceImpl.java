package com.photobooking.mediastaff.service;

import com.photobooking.mediastaff.dto.MediaStaffRequestDto;
import com.photobooking.mediastaff.dto.MediaStaffResponseDto;
import com.photobooking.mediastaff.entity.MediaStaff;
import com.photobooking.mediastaff.entity.StaffType;
import com.photobooking.mediastaff.exception.MediaStaffNotFoundException;
import com.photobooking.mediastaff.repository.MediaStaffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaStaffServiceImpl implements MediaStaffService {

    private final MediaStaffRepository mediaStaffRepository;
    private final ModelMapper modelMapper;

    @Override
    public MediaStaffResponseDto addStaff(MediaStaffRequestDto dto) {
        MediaStaff staff = MediaStaff.builder()
                .name(dto.getName())
                .type(StaffType.valueOf(dto.getType().toUpperCase()))
                .specialization(dto.getSpecialization())
                .pricePerDay(dto.getPricePerDay())
                .availability(dto.getAvailability())
                .contactInfo(dto.getContactInfo())
                .isAvailable(true)
                .build();

        return mapToDto(mediaStaffRepository.save(staff));
    }

    @Override
    public MediaStaffResponseDto getStaffById(Long id) {
        return mapToDto(mediaStaffRepository.findById(id)
                .orElseThrow(() -> new MediaStaffNotFoundException(id)));
    }

    @Override
    public List<MediaStaffResponseDto> getAllStaff() {
        return mediaStaffRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaStaffResponseDto> getByType(String type) {
        StaffType staffType = StaffType.valueOf(type.toUpperCase());
        return mediaStaffRepository.findByType(staffType).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaStaffResponseDto> getAvailableStaff() {
        return mediaStaffRepository.findByIsAvailableTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MediaStaffResponseDto updateStaff(Long id, MediaStaffRequestDto dto) {
        MediaStaff staff = mediaStaffRepository.findById(id)
                .orElseThrow(() -> new MediaStaffNotFoundException(id));

        staff.setName(dto.getName());
        staff.setType(StaffType.valueOf(dto.getType().toUpperCase()));
        staff.setSpecialization(dto.getSpecialization());
        staff.setPricePerDay(dto.getPricePerDay());
        staff.setAvailability(dto.getAvailability());
        staff.setContactInfo(dto.getContactInfo());

        return mapToDto(mediaStaffRepository.save(staff));
    }

    @Override
    public void removeStaff(Long id) {
        if (!mediaStaffRepository.existsById(id)) {
            throw new MediaStaffNotFoundException(id);
        }
        mediaStaffRepository.deleteById(id);
    }

    @Override
    public void toggleAvailability(Long id) {
        MediaStaff staff = mediaStaffRepository.findById(id)
                .orElseThrow(() -> new MediaStaffNotFoundException(id));
        staff.setAvailable(!staff.isAvailable());
        mediaStaffRepository.save(staff);
    }

    private MediaStaffResponseDto mapToDto(MediaStaff staff) {
        return MediaStaffResponseDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .type(staff.getType().name())
                .specialization(staff.getSpecialization())
                .pricePerDay(staff.getPricePerDay())
                .availability(staff.getAvailability())
                .contactInfo(staff.getContactInfo())
                .isAvailable(staff.isAvailable())
                .createdAt(staff.getCreatedAt())
                .build();
    }
}
