package com.engTalkMo.domain.user;

/**
 * 회원가입
 * 회원탈퇴
 * 정보수정
 * 회원조회
 */
public interface UserService {

    User signUp(UserDto userDto);
    //...
}
