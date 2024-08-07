package com.engtalkmo.domain.member.api;

import com.engtalkmo.domain.member.dto.AddMemberRequest;
import com.engtalkmo.domain.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody @Valid final AddMemberRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberSignUpService.signUp(dto));
    }
}
