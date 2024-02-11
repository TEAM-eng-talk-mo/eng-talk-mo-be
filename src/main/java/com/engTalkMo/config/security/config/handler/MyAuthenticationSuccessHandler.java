package com.engTalkMo.config.security.config.handler;

import com.engTalkMo.config.security.JwtUtil;
import com.engTalkMo.config.security.dto.GeneratedToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");

        boolean isExist = Boolean.TRUE.equals(oAuth2User.getAttribute("exist"));

        String role = oAuth2User.getAuthorities().stream()
                .findFirst() // 첫번째 Role 을 찾아온다.
                .orElseThrow(IllegalAccessError::new)
                .getAuthority();

        if (isExist) { // 회원이 존재하면 jwt token 발행을 시작한다.
            GeneratedToken token = jwtUtil.generateToken(email, role);
            log.info("jwtToken={}", token.getAccessToken());

            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/loginSuccess")
                    .queryParam("accessToken", token.getAccessToken())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else { // 회원이 존재하지 않을경우, 서비스 제공자와 email 을 쿼리스트링으로 전달하는 url 을 만들어준다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/loginSuccess")
                    .queryParam("email", (String) oAuth2User.getAttribute("email"))
                    .queryParam("provider", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

}