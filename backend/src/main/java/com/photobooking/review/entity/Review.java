package com.photobooking.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long staffId;
    private Long bookingId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;

    @CreationTimestamp
    private LocalDateTime reviewDate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
