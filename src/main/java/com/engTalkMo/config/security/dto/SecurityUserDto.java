package com.engTalkMo.config.security.dto;

import lombok.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @ToString
public class SecurityUserDto {

    private Long id;
    private String email;
    private String nickname;
    private String picture;
    private String role;
}