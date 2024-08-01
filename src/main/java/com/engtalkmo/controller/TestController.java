package com.engtalkmo.controller;

import com.engtalkmo.domain.member.Member;
import com.engtalkmo.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;

    @GetMapping("/test")
    public ResponseEntity<Result<List<MemberDto>>> findMembers() {
        List<MemberDto> list = memberRepository.findAll()
                .stream()
                .map(MemberDto::new)
                .toList();
        return ResponseEntity.ok(new Result<>(list.size(), list));
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private long count;
        private T data;
    }

    @Getter
    static class MemberDto {
        private final String name;

        public MemberDto(Member member) {
            this.name = member.getName();
        }
    }
}
