package com.engTalkMo.domain;

import jakarta.validation.constraints.NotEmpty;

public record MemberDto(
        @NotEmpty String username,
        @NotEmpty String password
) {}
