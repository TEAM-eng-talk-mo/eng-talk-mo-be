package com.engtalkmo.domain.member.exception;

import com.engtalkmo.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException(String value) {
        super(value + " is not found");
    }
}
