package com.example.healthcare.exceptions;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found");
    }
}
