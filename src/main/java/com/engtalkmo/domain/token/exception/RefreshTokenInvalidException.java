package com.engtalkmo.domain.token.exception;

import com.engtalkmo.global.error.exception.ErrorCode;
import com.engtalkmo.global.error.exception.InvalidValueException;

public class RefreshTokenInvalidException extends InvalidValueException {

    public RefreshTokenInvalidException(String refreshToken) {
        super(refreshToken + " is invalid", ErrorCode.INVALID_TOKEN);
    }
}
