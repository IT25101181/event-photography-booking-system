package com.photobooking.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private Long staffId;
    private Long bookingId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
    private LocalDateTime updatedAt;
}
