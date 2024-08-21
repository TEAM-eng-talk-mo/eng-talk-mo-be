package com.engtalkmo.global.config.oauth;

import com.engtalkmo.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Step #1) OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Step #2) Client(Google, Naver, Kakao) 등록 id 및 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // Step #3) OAuth2User -> OAuth2Attribute 생성
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // Step #4) 신규 회원인지 확인 후, 저장 또는 업데이트
        saveOrUpdate(oAuth2Attribute);

        // Step #5) DefaultOAuth2User 생성 후, 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2Attribute.attributes(),
                "email");
    }

    private void saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        memberRepository.findByEmail(oAuth2Attribute.email())
                .map(entity -> entity
                        .update(oAuth2Attribute.name()))
                .orElseGet(() -> memberRepository
                        .save(oAuth2Attribute.toEntity()));
    }
}
