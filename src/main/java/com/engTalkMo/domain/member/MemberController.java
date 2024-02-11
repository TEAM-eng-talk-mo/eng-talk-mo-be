package com.engTalkMo.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

//    @GetMapping("/members")
//    public List<Member> memberList() {
//        return memberRepository.findAll();
//    }
//
//    @PostMapping("/save")
//    public Member save(@RequestBody MemberDto memberDto) {
//        return memberService.save(memberDto);
//    }
}
