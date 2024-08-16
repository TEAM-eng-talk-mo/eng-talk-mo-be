package com.engtalkmo.domain.token.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccessTokenRequest(
        @NotBlank(message = "토큰이 존재하지 않습니다.") String refreshToken) {}
