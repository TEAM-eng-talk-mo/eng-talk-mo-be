package com.engTalkMo.domain.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserDto(
        @NotEmpty
        String username,
        @NotEmpty
        String password) {

    public User toEntity(UserDto userDto) {
        return User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .build();
    }
}
