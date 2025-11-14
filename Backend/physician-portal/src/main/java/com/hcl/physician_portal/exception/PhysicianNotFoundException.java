package com.hcl.physician_portal.exception;

public class PhysicianNotFoundException extends RuntimeException {
    public PhysicianNotFoundException(String message) {
        super(message);
    }

}
