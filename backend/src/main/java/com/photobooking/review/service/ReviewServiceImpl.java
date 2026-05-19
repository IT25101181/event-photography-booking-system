package com.photobooking.review.service;

import com.photobooking.review.dto.ReviewRequestDto;
import com.photobooking.review.dto.ReviewResponseDto;
import com.photobooking.review.entity.Review;
import com.photobooking.review.exception.ReviewNotFoundException;
import com.photobooking.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final com.photobooking.booking.repository.BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewResponseDto addReview(ReviewRequestDto dto) {
        // If booking ID is provided, we can still check for duplicates, but it's optional now
        if (dto.getBookingId() != null && reviewRepository.existsByBookingId(dto.getBookingId())) {
            throw new IllegalStateException("A review already exists for this booking");
        }

        Review review = Review.builder()
                .userId(dto.getUserId())
                .staffId(dto.getStaffId())
                .bookingId(dto.getBookingId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        return mapToDto(reviewRepository.save(review));
    }

    @Override
    public ReviewResponseDto getReviewById(Long id) {
        return mapToDto(reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id)));
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> getReviewsByStaff(Long staffId) {
        return reviewRepository.findByStaffId(staffId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageRatingForStaff(Long staffId) {
        Double avg = reviewRepository.findAverageRatingByStaffId(staffId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        if (!review.getUserId().equals(dto.getUserId())) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return mapToDto(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException(id);
        }
        reviewRepository.deleteById(id);
    }

    private ReviewResponseDto mapToDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .staffId(review.getStaffId())
                .bookingId(review.getBookingId())
                .rating(review.getRating())
                .comment(review.getComment())
                .reviewDate(review.getReviewDate())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
