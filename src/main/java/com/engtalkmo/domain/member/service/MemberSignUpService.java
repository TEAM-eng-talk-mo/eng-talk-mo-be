package com.engtalkmo.domain.member.service;

import com.engtalkmo.domain.member.entity.Member;
import com.engtalkmo.domain.member.dto.AddMemberRequest;
import com.engtalkmo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Long signUp(AddMemberRequest dto) {
        Member member = dto.toEntity(passwordEncoder);
        return memberRepository.save(member).getId();
    }
}
