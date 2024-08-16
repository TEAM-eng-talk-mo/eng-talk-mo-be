package com.engtalkmo.domain.token.service;

import com.engtalkmo.domain.token.entity.RefreshToken;
import com.engtalkmo.domain.token.exception.RefreshTokenNotFoundException;
import com.engtalkmo.domain.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenFindService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));
    }
}
