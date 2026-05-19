package com.photobooking.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Staff IDs are required")
    private java.util.List<Long> staffIds;

    private Long packageId;

    @NotNull(message = "Event date is required")
    private String eventDate; // yyyy-MM-dd format

    private String location;

    private double totalAmount;

    private String notes;
}
