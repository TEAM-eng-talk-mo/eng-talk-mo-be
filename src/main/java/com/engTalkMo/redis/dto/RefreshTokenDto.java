package com.engTalkMo.redis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RefreshTokenDto {

    @Id
    @GeneratedValue
    private Long id;
    private String token;
}
