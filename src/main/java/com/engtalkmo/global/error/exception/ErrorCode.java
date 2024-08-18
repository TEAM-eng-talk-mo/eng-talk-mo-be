package com.engtalkmo.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Commons Exception code
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    UNSUPPORTED(400, "C007", "Unsupported"),

    // Member Exception code
    EMAIL_DUPLICATION(400, "M001", "Email is duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    MEMBER_NOT_FOUND(400, "M003", "Member is not found"),

    // Token Exception code
    INVALID_TOKEN(400, "T001", "Token is invalid");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
