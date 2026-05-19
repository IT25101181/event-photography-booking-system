package com.photobooking.mediastaff.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StaffType type;

    private String specialization;

    private double pricePerDay;

    private String availability;

    private String contactInfo;

    @Builder.Default
    private boolean isAvailable = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
