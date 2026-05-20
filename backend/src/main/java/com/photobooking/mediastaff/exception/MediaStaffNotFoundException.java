package com.photobooking.mediastaff.exception;

public class MediaStaffNotFoundException extends RuntimeException {

    public MediaStaffNotFoundException(Long id) {
        super("Media staff not found with id: " + id);
    }
}
