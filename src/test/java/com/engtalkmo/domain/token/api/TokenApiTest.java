package com.engtalkmo.domain.token.api;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.domain.member.repository.MemberRepository;
import com.engtalkmo.domain.token.dto.CreateAccessTokenRequest;
import com.engtalkmo.domain.token.entity.RefreshToken;
import com.engtalkmo.domain.token.repository.RefreshTokenRepository;
import com.engtalkmo.global.config.jwt.JwtFactory;
import com.engtalkmo.global.config.jwt.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired WebApplicationContext context;
    @Autowired JwtProperties properties;

    @Autowired MemberRepository memberRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;

    private Member member;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        memberRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        memberRepository.deleteAll();
        member = memberRepository.save(Member.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(member.getEmail(), "", authorities);
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(user, member.getPassword(), authorities));
    }

    @DisplayName("createAccessToken(): 새로운 액세스 토큰을 발급한다.")
    @Test
    void createAccessToken() throws Exception {
        // given
        final String url = "/api/token";

        Member member = memberRepository.save(
                Member.builder()
                        .email("hui@emai.com")
                        .password("engtalkmo!!")
                        .build());
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(properties);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberId(member.getId())
                        .refreshToken(refreshToken)
                        .build());

        CreateAccessTokenRequest request = new CreateAccessTokenRequest(refreshToken);
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void deleteRefreshToken() throws Exception {
        // given
        refreshTokenRepository.deleteAll(); // fix: refreshToken 삭제
        final String url = "/api/refresh-token";

        String refreshToken = createRefreshToken();

        refreshTokenRepository.save(RefreshToken.builder()
                .memberId(member.getId())
                .refreshToken(refreshToken)
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(member.getEmail(), "", authorities);
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(user, refreshToken, authorities));

        // when
        ResultActions resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isOk());

        assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
    }

    private String createRefreshToken() {
        return JwtFactory.builder()
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(properties);
    }
}