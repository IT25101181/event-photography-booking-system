package com.photobooking.review.repository;

import com.photobooking.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStaffId(Long staffId);
    List<Review> findByUserId(Long userId);
    Optional<Review> findByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.staffId = :staffId")
    Double findAverageRatingByStaffId(@Param("staffId") Long staffId);

    boolean existsByBookingId(Long bookingId);
}
