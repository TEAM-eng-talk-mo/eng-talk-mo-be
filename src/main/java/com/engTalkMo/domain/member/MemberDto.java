package com.engTalkMo.domain.member;

import jakarta.validation.constraints.NotEmpty;

public record MemberDto(
        @NotEmpty String username,
        @NotEmpty String password
) {}
