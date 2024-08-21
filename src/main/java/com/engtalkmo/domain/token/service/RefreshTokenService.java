package com.engtalkmo.domain.token.service;

import com.engtalkmo.domain.token.entity.RefreshToken;
import com.engtalkmo.domain.token.exception.RefreshTokenNotFoundException;
import com.engtalkmo.domain.token.repository.RefreshTokenRepository;
import com.engtalkmo.global.config.jwt.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));
    }

    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext()
                .getAuthentication().getCredentials().toString();
        Long memberId = tokenProvider.getMemberId(token);
        refreshTokenRepository.deleteById(memberId);
    }
}
