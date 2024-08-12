package com.engtalkmo.domain.member.controller;

import com.engtalkmo.domain.member.dto.AddMemberRequest;
import com.engtalkmo.domain.member.service.MemberSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignUpService memberSignUpService;

    @PostMapping("/members")
    public String signup(AddMemberRequest request) {
        memberSignUpService.signUp(request);
        return "redirect:/login";
    }
}
