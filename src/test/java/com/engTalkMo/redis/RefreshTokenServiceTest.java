package com.engTalkMo.redis;

import com.engTalkMo.redis.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RefreshTokenServiceTest {

    @Mock RefreshTokenRepository refreshTokenRepository;

    @InjectMocks RefreshTokenService refreshTokenService;

    @Test
    public void test1(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("이재권 테스트");

        Mockito.when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshToken);

        RefreshToken saveRefreshToken = refreshTokenService.createRefreshToken(refreshToken);

        assertEquals(refreshToken.getId(), saveRefreshToken.getId());
        assertEquals(refreshToken.getToken(), saveRefreshToken.getToken());
    }
    @Test
    public void test2(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("이재권 테스트");
        Mockito.when(refreshTokenRepository.findByToken("이재권 테스트")).thenReturn(Optional.of(refreshToken));

        RefreshToken saveRefreshToken = refreshTokenService.getRefreshToken("이재권 테스트");

        assertEquals(refreshToken.getId(), saveRefreshToken.getId());
        assertEquals(refreshToken.getToken(), saveRefreshToken.getToken());
    }
}
