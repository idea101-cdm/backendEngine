package com.idea101.backendengine.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidOtpException extends ApiException {
    public InvalidOtpException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "OTP_INVALID");
    }
}
