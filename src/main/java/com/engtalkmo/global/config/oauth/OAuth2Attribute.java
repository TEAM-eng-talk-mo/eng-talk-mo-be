package com.engtalkmo.global.config.oauth;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.global.error.exception.UnsupportedSocialLoginException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Builder
public record OAuth2Attribute(
        Map<String, Object> attributes,
        String attributeKey,
        String email,
        String name,
        String picture,
        String provider) {


    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(provider, attributeKey, attributes);
            case "naver" -> ofNaver(provider, attributeKey, attributes);
            case "kakao" -> ofKakao(provider, attributeKey, attributes);
            default -> throw new UnsupportedSocialLoginException("제공하지 않는 소셜 로그인입니다.");
        };
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuth2Attribute ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
        // Naver 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 response Map 에 감싸져 있어 꺼낸 후, 작업해야한다.
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .picture((String) response.get("profile_image"))
                .provider(provider)
                .attributes(response)
                .attributeKey(attributeKey)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuth2Attribute ofKakao(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.get("nickname"))
                .picture((String) profile.get("profile_image_url"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }
}