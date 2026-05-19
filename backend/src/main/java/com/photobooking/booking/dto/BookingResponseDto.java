package com.photobooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private java.util.List<Long> staffIds;
    private java.util.List<String> staffNames;
    private Long packageId;
    private LocalDate eventDate;
    private String location;
    private String status;
    private double totalAmount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
