package com.engtalkmo.global.error.exception;

import static com.engtalkmo.global.error.exception.ErrorCode.UNSUPPORTED;

public class UnsupportedSocialLoginException extends BusinessException {

    public UnsupportedSocialLoginException(String message) {
        super(message, UNSUPPORTED);
    }

    public UnsupportedSocialLoginException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
