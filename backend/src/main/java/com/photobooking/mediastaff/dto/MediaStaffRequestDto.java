package com.photobooking.mediastaff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MediaStaffRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type; // PHOTOGRAPHER or VIDEOGRAPHER

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Positive(message = "Price per day must be positive")
    private double pricePerDay;

    private String availability;

    private String contactInfo;
}
