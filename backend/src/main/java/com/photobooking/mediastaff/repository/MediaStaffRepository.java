package com.photobooking.mediastaff.repository;

import com.photobooking.mediastaff.entity.MediaStaff;
import com.photobooking.mediastaff.entity.StaffType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaStaffRepository extends JpaRepository<MediaStaff, Long> {

    List<MediaStaff> findByType(StaffType type);

    List<MediaStaff> findByIsAvailableTrue();

    List<MediaStaff> findByTypeAndIsAvailableTrue(StaffType type);
}
