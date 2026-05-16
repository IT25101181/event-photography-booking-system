package com.photobooking.review.controller;

import com.photobooking.review.dto.ReviewRequestDto;
import com.photobooking.review.dto.ReviewResponseDto;
import com.photobooking.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(reviewService.getReviewsByStaff(staffId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    @GetMapping("/staff/{staffId}/rating")
    public ResponseEntity<Double> getAverageRatingForStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(reviewService.getAverageRatingForStaff(staffId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
