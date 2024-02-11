package com.engTalkMo.config.security.config;

import com.engTalkMo.config.security.config.handler.MyAuthenticationFailureHandler;
import com.engTalkMo.config.security.config.handler.MyAuthenticationSuccessHandler;
import com.engTalkMo.config.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * <pre>
 * public class SecurityConfig extends WebSecurityConfigurerAdapter {}
 * WebSecurityConfigurerAdapter 를 상속받고 메서드를 오버라이딩하여 설정하는 방식은 deprecated 되어 권장되지 않는 방식이다.
 * 공식 문서에서 권장하는 방식은 SecurityFilterChain 을 Bean 으로 등록하여 사용하는 방식을 권장하고 있으며, 이를 바탕으로 설정한다.
 * </pre>
 * <pre>
 * HTTP 기본 인증 비활성화
 *  - Spring Security 기본 로그인 화면 사용하지 않으며, Session 을 사용하지 않고, Rest API 방식을 사용하기 때문에 비활성화한다.
 * CORS 활성화 & CSRF 보호 기능 비활성화
 *  - FE 에서 Next.js 를 협업하여 사용하기 때문에 CORS 문제를 해결하기 위해 활성화한다.
 *    CSRF 는 사이트 간 위조 요청 공격 방법 중 하나로 Spring Security 공식 문서 상 non-browser clients 만을 위한 서비스라면
 *    CSRF 를 비활성화시켜도 괜찮다고 말하기 때문에 Rest API 방식 만을 사용하는 서버이므로 비활성화한다.
 * 세션관리 정책 STATELESS
 *  - STATELESS 는 Spring Security 가 Session 을 생성하지 않고, 있어도 사용하지 않겠다는 설정 값이다.
 * </pre>
 */
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화 (Session 을 사용하지 않고, Rest API 방식을 사용)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(http)) // CORS 활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 기능 비활성화
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(STATELESS)) // 세션관리 정책 STATELESS (세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다.)
                .authorizeHttpRequests((authorize -> authorize
                        .requestMatchers("/token/**").permitAll() // 토큰 발급을 위한 경로는 모두 허용
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())) // 그 외의 모든 요청은 인증 필요
                // .hasAnyRole(Role.USER.name(), Role.ADMIN.name())))
                .oauth2Login(login -> login
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService)) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정
                        .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공시 처리할 핸들러
                        .failureHandler(oAuth2LoginFailureHandler)) // OAuth2 로그인 실패시 처리할 핸들러
                .build();

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        return http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
