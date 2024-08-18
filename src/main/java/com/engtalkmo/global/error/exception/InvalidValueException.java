package com.engtalkmo.global.error.exception;

import static com.engtalkmo.global.error.exception.ErrorCode.INVALID_INPUT_VALUE;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String value) {
        super(value, INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}