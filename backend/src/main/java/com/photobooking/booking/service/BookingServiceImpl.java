package com.photobooking.booking.service;

import com.photobooking.booking.dto.BookingRequestDto;
import com.photobooking.booking.dto.BookingResponseDto;
import com.photobooking.booking.entity.Booking;
import com.photobooking.booking.entity.BookingStatus;
import com.photobooking.booking.exception.BookingNotFoundException;
import com.photobooking.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.photobooking.auth.entity.AppUser;
import com.photobooking.admin.entity.AdminUser;
import com.photobooking.auth.repository.AppUserRepository;
import com.photobooking.mediastaff.repository.MediaStaffRepository;
import com.photobooking.mediastaff.entity.MediaStaff;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AppUserRepository appUserRepository;
    private final MediaStaffRepository mediaStaffRepository;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        LocalDate eventDate = LocalDate.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Booking booking = Booking.builder()
                .userId(dto.getUserId())
                .staffIds(dto.getStaffIds())
                .packageId(dto.getPackageId())
                .eventDate(eventDate)
                .location(dto.getLocation())
                .totalAmount(dto.getTotalAmount())
                .notes(dto.getNotes())
                .status(BookingStatus.PENDING)
                .build();

        return mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || 
                          auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        
        if (isAdmin) return mapToDto(booking);

        Object principal = auth.getPrincipal();
        Long currentUserId = null;
        if (principal instanceof AppUser) currentUserId = ((AppUser) principal).getId();
        else if (principal instanceof AdminUser) currentUserId = ((AdminUser) principal).getId();

        if (currentUserId != null && currentUserId.equals(booking.getUserId())) {
            return mapToDto(booking);
        }

        throw new org.springframework.security.access.AccessDeniedException("You do not have permission to view this booking");
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return List.of();

        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || 
                          auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        
        if (isAdmin) {
            return bookingRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        Object principal = auth.getPrincipal();
        Long userId = null;
        if (principal instanceof AppUser) {
            userId = ((AppUser) principal).getId();
        } else if (principal instanceof AdminUser) {
            userId = ((AdminUser) principal).getId();
        }

        if (userId != null) {
            return getBookingsByUser(userId);
        }

        return List.of();
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByStatus(String status) {
        BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        return bookingRepository.findByStatus(bookingStatus).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be confirmed. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        return mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only PENDING or CONFIRMED bookings can be cancelled. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED bookings can be completed. Current status: " + booking.getStatus());
        }

        if (booking.getEventDate() != null && booking.getEventDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Upcoming events cannot be completed before the event date.");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto updateBooking(Long id, BookingRequestDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        // Only allow update if PENDING or CONFIRMED
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a booking that is " + booking.getStatus());
        }

        if (dto.getEventDate() != null) {
            booking.setEventDate(LocalDate.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (dto.getLocation() != null) {
            booking.setLocation(dto.getLocation());
        }
        if (dto.getNotes() != null) {
            booking.setNotes(dto.getNotes());
        }
        if (dto.getStaffIds() != null) {
            booking.setStaffIds(dto.getStaffIds());
        }
        if (dto.getPackageId() != null) {
            booking.setPackageId(dto.getPackageId());
        }
        
        // Always update total amount if passed, or we could recalculate it here
        if (dto.getTotalAmount() > 0) {
            booking.setTotalAmount(dto.getTotalAmount());
        }

        return mapToDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }

    private BookingResponseDto mapToDto(Booking booking) {
        String userName = "Unknown";
        if (booking.getUserId() != null) {
            userName = appUserRepository.findById(booking.getUserId())
                    .map(AppUser::getFullName)
                    .orElse("Unknown");
        }

        List<String> staffNames = List.of();
        if (booking.getStaffIds() != null && !booking.getStaffIds().isEmpty()) {
            staffNames = mediaStaffRepository.findAllById(booking.getStaffIds()).stream()
                    .map(MediaStaff::getName)
                    .collect(Collectors.toList());
        }

        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .userName(userName)
                .staffIds(booking.getStaffIds())
                .staffNames(staffNames)
                .packageId(booking.getPackageId())
                .eventDate(booking.getEventDate())
                .location(booking.getLocation())
                .status(booking.getStatus().name())
                .totalAmount(booking.getTotalAmount())
                .notes(booking.getNotes())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
