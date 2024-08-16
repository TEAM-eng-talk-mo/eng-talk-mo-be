package com.engtalkmo.global.config.jwt;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class TokenProviderTest {

    @Autowired private TokenProvider tokenProvider;
    @Autowired private MemberRepository memberRepository;
    @Autowired JwtProperties jwtProperties;

    @DisplayName("generateToken(): 회원 정보와 만료 기간을 전달하여 토큰을 만든다.")
    @Test
    void generateToken() {
        // given
        Member testMember = memberRepository.save(
                Member.builder()
                        .email("hui@email.com")
                        .password("engtalkmo!")
                        .build());

        // when
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        log.info("token={}", token);

        // then
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        log.info("claims={}", claims);

        assertThat(claims.get("id", Long.class)).isEqualTo(testMember.getId());
        assertThat(claims.getSubject()).isEqualTo(testMember.getEmail());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우 검증을 실패한다.")
    @Test
    void validToken_fail() {
        // given
        String token = JwtFactory.builder()
                .expiresAt(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰일 경우 검증에 성공한다.")
    @Test
    void validToken_success() {
        // given
        String token = JwtFactory
                .withDefaultValues()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져온다.")
    @Test
    void getAuthentication() {
        // given
        String email = "hui@email.com";
        String token = JwtFactory.builder()
                .subject(email)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication auth = tokenProvider.getAuthentication(token);

        // then
        UserDetails principal = (UserDetails) auth.getPrincipal();
        assertThat(principal.getUsername()).isEqualTo(email);

        log.info("principal={}", principal);
    }

    @DisplayName("getMemberId(): 토큰으로 회원 ID를 가져올 수 있다.")
    @Test
    void getMemberId() {
        // given
        long memberId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", memberId))
                .build()
                .createToken(jwtProperties);

        // when
        Long memberIdByToken = tokenProvider.getMemberId(token);

        // then
        assertThat(memberIdByToken).isEqualTo(memberId);
    }
}