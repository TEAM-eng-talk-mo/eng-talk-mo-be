package com.engTalkMo.config.security;

import com.engTalkMo.config.properties.JwtProperties;
import com.engTalkMo.config.security.dto.GeneratedToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {

    private String secretKey;
    private final JwtProperties jwtProperties;
    // private final AccessTokenService tokenService; 미생성

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public GeneratedToken generateToken(String email, String role) {
        // refreshToken 과 accessToken 을 생성
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        /* TODO 토큰 저장 방식 NoSQL(Redis) vs SQL(RDB) ?
         * Redis 공부를 안해서 사용할 줄 모름 근데 토큰 값을 저장하고, 지우는데 사용되는 리소스가 RDS 보다 유리 (프로젝트라 부하 올 일이 없긴 한데...)
         * refresh 토큰 값은 2주 간격으로 삭제하는 방식으로 설계했는데 RDB 사용 시 따로 스케줄러를 통해 만료시키는 방법을 택해야 함
         * Redis 는 TTL(Time-To-Live)라는 기능이 있어, 데이터를 저장하고 설정한 TTL 에 따라서 자동으로 삭제되는 옵션이 있다고 함... (미치겠네 씨발)
         * 일단 토큰 저장/삭제 방식 보류...
         */
        // tokenService.saveTokenInfo(email, refreshToken, accessToken);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateRefreshToken(String email, String role) {
        long refreshPeriod = 1000L * 60L * 60L * 24L * 14; // 토큰의 유효 기간을 밀리초 단위로 설정 (2주)

        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한) 설정
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // Payload 를 구성하는 속성들을 정의
                .setIssuedAt(now) // 발행일자
                .setExpiration(new Date(now.getTime() + refreshPeriod)) // 토큰의 만료일시를 설정 (2주)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명
                .compact();
    }

    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 30L; // 30분
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // Payload 를 구성하는 속성들을 정의
                .setIssuedAt(now) // 발행일자
                .setExpiration(new Date(now.getTime() + tokenPeriod)) // 토큰의 만료일시를 설정 (30분)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명
                .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey) // 비밀키를 설정하여 파싱
                    .parseClaimsJws(token); // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.
            return claims.getBody()
                    .getExpiration()
                    .after(new Date()); // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과를 반환
        } catch (Exception e) {
            log.info("토큰만료={}", token);
            return false;
        }
    }

    public String getUid(String token) { // 토큰에서 Email 추출
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) { // 토큰에서 ROLE(권한) 추출
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }
}
