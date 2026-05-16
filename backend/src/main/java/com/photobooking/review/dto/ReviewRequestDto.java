package com.photobooking.review.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;
}
