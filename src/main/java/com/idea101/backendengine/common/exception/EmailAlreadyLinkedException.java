package com.idea101.backendengine.common.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyLinkedException extends ApiException {
    public EmailAlreadyLinkedException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_LINKED");
    }
}
