package com.engTalkMo.redis;

import com.engTalkMo.redis.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Cacheable(value = "RefreshToken", key = "#token", cacheManager = "redisCacheManager")
    public RefreshToken getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(()->new RuntimeException("토큰값이 없습니다."));
    }

    @Override
    @CacheEvict(value = "RefreshToken", key = "#token", cacheManager = "redisCacheManager")
    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->new RuntimeException("토큰값이 없습니다."));
        refreshTokenRepository.delete(refreshToken);
    }
}
