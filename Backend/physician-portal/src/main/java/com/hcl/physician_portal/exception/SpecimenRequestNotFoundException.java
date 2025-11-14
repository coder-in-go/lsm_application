package com.hcl.physician_portal.exception;


public class SpecimenRequestNotFoundException extends RuntimeException {
    public SpecimenRequestNotFoundException(String message) {
        super(message);
    }
}
