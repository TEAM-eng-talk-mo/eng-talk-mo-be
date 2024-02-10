package com.engTalkMo.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member save(MemberDto memberDto) {
        Member member = Member.of(memberDto);
        memberRepository.save(member);
        return member;
    }
}
