package com.engTalkMo.config.security.filter;

import com.engTalkMo.config.security.JwtUtil;
import com.engTalkMo.config.security.dto.SecurityUserDto;
import com.engTalkMo.domain.member.MemberRepository;
import com.engTalkMo.domain.member.entity.Member;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String atc = request.getHeader("Authorization");
        if (!StringUtils.hasText(atc)) { // 토큰 검사 생략(모든 허용 URL 의 경우 토큰 검사 통과)
            doFilter(request, response, filterChain);
            return;
        }

        if (!jwtUtil.verifyToken(atc)) { // AccessToken 검증 후, 만료되었을경우 예외를 발생시킨다.
            throw new JwtException("Access Token 값이 만료되었습니다.");
        }

        if (jwtUtil.verifyToken(atc)) { // AccessToken 값이 있고, 유효한 경우에 진행한다.

            // AccessToken 내부의 payload 에 있는 email 로 member 를 조회한다. 없다면 예외를 발생시킨다.
            Member findMember = memberRepository.findByEmail(jwtUtil.getUid(atc))
                    .orElseThrow(IllegalStateException::new);

            // SecurityContext 에 등록할 User 객체를 만들어준다.
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .id(findMember.getId())
                    .email(findMember.getEmail())
                    .role(findMember.getRole().name())
                    .nickname(findMember.getNickname())
                    .build();

            // SecurityContext 에 인증 객체를 등록한다.
            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority(member.getRole())));
    }
}
