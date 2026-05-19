package com.photobooking.mediastaff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaStaffResponseDto {

    private Long id;
    private String name;
    private String type;
    private String specialization;
    private double pricePerDay;
    private String availability;
    private String contactInfo;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
    private LocalDateTime createdAt;
}
