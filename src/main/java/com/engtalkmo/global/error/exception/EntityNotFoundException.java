package com.engtalkmo.global.error.exception;

import static com.engtalkmo.global.error.exception.ErrorCode.ENTITY_NOT_FOUND;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ENTITY_NOT_FOUND);
    }
}
