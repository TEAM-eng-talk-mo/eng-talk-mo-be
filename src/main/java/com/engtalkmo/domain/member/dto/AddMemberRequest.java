package com.engtalkmo.domain.member.dto;

import com.engtalkmo.domain.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record AddMemberRequest(
        @NotEmpty(message = "이메일은 필수 입력입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotEmpty(message = "이름은 필수 입력입니다.")
        String name,

        @NotEmpty(message = "이메일은 필수 입력입니다.")
        String password
) {

    public Member toEntity(BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .password(passwordEncoder.encode(this.password))
                .build();
    }
}
