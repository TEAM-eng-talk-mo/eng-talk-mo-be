package com.engTalkMo.config.security.config;

import com.engTalkMo.config.security.dto.OAuth2Attribute;
import com.engTalkMo.domain.member.MemberRepository;
import com.engTalkMo.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Default OAuth2UserService 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService 를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // Client 등록 ID(Google, Naver, KAKAO)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService 를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 를 만든다.
        OAuth2Attribute attributes = OAuth2Attribute.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // attributes 를 이용하여 권한, 속성, 이름을 이용해 DefaultOAuth2User 를 생성해 반환한다.
        return getDefaultOAuth2User(attributes);
    }

    private OAuth2User getDefaultOAuth2User(OAuth2Attribute attributes) {
        Map<String, Object> memberAttribute = attributes.convertToMap();
        String email = (String) memberAttribute.get("email");
        Optional<Member> findMember = memberRepository.findByEmail(email);

        String authority;
        if (findMember.isEmpty()) {
            memberAttribute.put("exist", false);
            authority = "USER";
        } else {
            memberAttribute.put("exist", true);
            authority = findMember.get().getRole().name();
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(authority)),
                memberAttribute, "email");
    }
}
