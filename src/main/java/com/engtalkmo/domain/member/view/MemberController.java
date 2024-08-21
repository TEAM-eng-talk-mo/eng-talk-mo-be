package com.engtalkmo.domain.member.view;

import com.engtalkmo.domain.member.dto.AddMemberRequest;
import com.engtalkmo.domain.member.service.MemberSignUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    // @GetMapping("/logout")
    // public String logout(HttpServletRequest request, HttpServletResponse response) {
    //     SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    //     logoutHandler.logout(
    //             request, response, SecurityContextHolder.getContext().getAuthentication());
    //     return "redirect:/login";
    // }
}
