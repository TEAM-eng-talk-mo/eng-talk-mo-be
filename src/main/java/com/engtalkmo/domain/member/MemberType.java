package com.engtalkmo.domain.member;

public enum MemberType {

    ENGTALK, GOOGLE, NAVER, KAKAO;

    public static MemberType from(String type) {
        return MemberType.valueOf(type.toUpperCase());
    }
}
