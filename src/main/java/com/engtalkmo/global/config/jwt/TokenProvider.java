package com.engtalkmo.global.config.jwt;

import com.engtalkmo.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties properties;

    public String generateToken(Member member, Duration duration) {
        Date expiry = new Date(new Date().getTime() + duration.toMillis());
        return createToken(expiry, member);
    }

    private String createToken(Date expiry, Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(member.getEmail())
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(properties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("id", member.getId())
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey())
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(properties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(
                new User(claims.getSubject(), "", authorities), token, authorities);
    }

    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(properties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
