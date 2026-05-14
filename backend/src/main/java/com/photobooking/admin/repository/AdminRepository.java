package com.photobooking.admin.repository;

import com.photobooking.admin.entity.AdminRole;
import com.photobooking.admin.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<AdminUser> findByIsActiveTrue();
    List<AdminUser> findByRole(AdminRole role);
}
