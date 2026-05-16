package com.photobooking.review.service;

import com.photobooking.review.dto.ReviewRequestDto;
import com.photobooking.review.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto addReview(ReviewRequestDto dto);
    ReviewResponseDto getReviewById(Long id);
    List<ReviewResponseDto> getAllReviews();
    List<ReviewResponseDto> getReviewsByStaff(Long staffId);
    List<ReviewResponseDto> getReviewsByUser(Long userId);
    Double getAverageRatingForStaff(Long staffId);
    ReviewResponseDto updateReview(Long id, ReviewRequestDto dto);
    void deleteReview(Long id);
}
