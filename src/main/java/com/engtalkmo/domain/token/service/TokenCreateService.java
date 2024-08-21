package com.engtalkmo.domain.token.service;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.domain.member.service.MemberFindService;
import com.engtalkmo.domain.token.exception.RefreshTokenInvalidException;
import com.engtalkmo.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenCreateService {

    private static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofHours(2);

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberFindService memberFindService;

    public String createAccessToken(String refreshToken) {

        if (!tokenProvider.validToken(refreshToken)) {
            throw new RefreshTokenInvalidException(refreshToken);
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberFindService.findById(memberId);

        return tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRY);
    }
}
