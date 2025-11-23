package com.idea101.backendengine.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateAccountCreation extends ApiException {
    public DuplicateAccountCreation(String message) {
        super(message, HttpStatus.BAD_REQUEST, "DUPLICATE_ACCOUNT_CREATION");
    }
}
