package com.photobooking.admin.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long id) {
        super("Admin not found with id: " + id);
    }
    
    public AdminNotFoundException(String message) {
        super(message);
    }
}
