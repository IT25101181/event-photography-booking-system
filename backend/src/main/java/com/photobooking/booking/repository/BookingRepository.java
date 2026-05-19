package com.photobooking.booking.repository;

import com.photobooking.booking.entity.Booking;
import com.photobooking.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStaffIds(Long staffId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByEventDateBetween(LocalDate start, LocalDate end);
}
