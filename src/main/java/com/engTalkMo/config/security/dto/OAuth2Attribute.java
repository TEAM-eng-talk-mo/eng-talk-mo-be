package com.engTalkMo.config.security.dto;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public record OAuth2Attribute(
        Map<String, Object> attributes,
        String attributeKey,
        String name,
        String email,
        String picture,
        String provider) {

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(provider, attributeKey, attributes);
            case "naver" -> ofNaver(provider, attributes);
            // case "kakao" -> ofKakao(); 구현 필요!!
            default -> throw new RuntimeException("제공하지 않는 서비스입니다.");
        };
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    @SuppressWarnings("unchecked") // 형변환 경고 무시
    private static OAuth2Attribute ofNaver(String provider, Map<String, Object> attributes) {
        // Naver 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 response Map 에 감싸져 있어 꺼낸 후, 작업해야한다.
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .attributes(response)
                .provider(provider)
                .attributeKey("id")
                .build();
    }

    private static OAuth2Attribute ofKakao() {
        //... 구현 필요
        return OAuth2Attribute.builder().build();
    }

    /**
     * OAuth2User 객체에 넣어주기 위해서 Map 으로 값들을 반환해준다.
     * @return Map<String, Object>
     */
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);
        return map;
    }
}
