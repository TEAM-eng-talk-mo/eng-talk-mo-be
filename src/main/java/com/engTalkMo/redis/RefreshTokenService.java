package com.engTalkMo.redis;

import com.engTalkMo.redis.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken (RefreshToken refreshToken);

    public RefreshToken getRefreshToken(String token);

    public void deleteRefreshToken(String token);
}
