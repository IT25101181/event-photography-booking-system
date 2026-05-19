package com.photobooking.booking.service;

import com.photobooking.booking.dto.BookingRequestDto;
import com.photobooking.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto dto);
    BookingResponseDto getBookingById(Long id);
    List<BookingResponseDto> getAllBookings();
    List<BookingResponseDto> getBookingsByUser(Long userId);
    List<BookingResponseDto> getBookingsByStatus(String status);
    BookingResponseDto confirmBooking(Long id);
    BookingResponseDto cancelBooking(Long id);
    BookingResponseDto completeBooking(Long id);
    BookingResponseDto updateBooking(Long id, BookingRequestDto dto);
    void deleteBooking(Long id);
}
