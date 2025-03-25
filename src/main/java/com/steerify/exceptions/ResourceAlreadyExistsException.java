package com.steerify.exceptions;

import jakarta.validation.constraints.NotBlank;

public class ResourceAlreadyExistsException extends Throwable {
    public ResourceAlreadyExistsException( String message) {
        super(message);
    }
}
