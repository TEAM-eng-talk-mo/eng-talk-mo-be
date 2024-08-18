package com.engtalkmo.global.config.oauth;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.domain.member.service.MemberFindService;
import com.engtalkmo.domain.token.entity.RefreshToken;
import com.engtalkmo.domain.token.repository.RefreshTokenRepository;
import com.engtalkmo.global.config.jwt.TokenProvider;
import com.engtalkmo.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    public static final String REDIRECT_PATH = "/articles";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthRequestBasedOnCookieRepository authCookieRequestRepository;
    private final MemberFindService memberFindService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = memberFindService.findByEmail(
                (String) oAuth2User.getAttributes().get("email"));

        // Step #1) RefreshToken 생성 & DB 저장 -> RefreshToken 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // Step #2) AccessToken 생성 -> url query parameter: AccessToken 추가
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // Step #3) 인증 관련 설정(oauth2_request) 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // Step #4) Redirect ~/?token={access_token}
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(e -> e.update(newRefreshToken))
                .orElse(RefreshToken.builder()
                        .memberId(memberId)
                        .refreshToken(newRefreshToken)
                        .build());

        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    private String getTargetUrl(String accessToken) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authCookieRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
