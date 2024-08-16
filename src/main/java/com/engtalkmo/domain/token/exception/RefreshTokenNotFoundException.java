package com.engtalkmo.domain.token.exception;

import com.engtalkmo.global.error.exception.EntityNotFoundException;

public class RefreshTokenNotFoundException extends EntityNotFoundException {

    public RefreshTokenNotFoundException(String refreshToken) {
        super(refreshToken + " is not found");
    }
}
