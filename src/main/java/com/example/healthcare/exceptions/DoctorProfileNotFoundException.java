package com.example.healthcare.exceptions;

public class DoctorProfileNotFoundException extends RuntimeException {

    public DoctorProfileNotFoundException() {
        super();
    }

    public DoctorProfileNotFoundException(String message) {
        super(message);
    }

    public DoctorProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
