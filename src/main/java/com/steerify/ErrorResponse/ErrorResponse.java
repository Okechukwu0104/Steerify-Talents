package com.steerify.ErrorResponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String message, int value) {
        this.message = message;
        this.status = value;
    }
}
