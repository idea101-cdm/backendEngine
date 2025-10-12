package com.idea101.backendengine.common.exception;

import org.springframework.http.HttpStatus;

public class PhoneAlreadyLinkedException extends ApiException {
    public PhoneAlreadyLinkedException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "PHONE_ALREADY_LINKED");
    }
}
