package com.idea101.backendengine.common.exception;

import org.springframework.http.HttpStatus;

public class OtpExpiredException extends ApiException{
    public OtpExpiredException(String message) {
        super(message, HttpStatus.GONE, "OTP_EXPIRED");
    }
}
