package com.engTalkMo.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    void toEntity() {
        //given
        String username = "user";
        String password = "password";
        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .build();

        //when
        User user = userDto.toEntity(userDto);

        //then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
    }
}