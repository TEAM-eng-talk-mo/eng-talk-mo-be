package com.engtalkmo.controller;

import com.engtalkmo.domain.member.Member;
import com.engtalkmo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class TestControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
    }

    @DisplayName("findMembers(): 회원목록 조회 성공")
    @Test
    void findMembers() throws Exception {
        // given
        final String url = "/test";
        final Member member = memberRepository.save(new Member("홍길동"));

        // when
        ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.data[0].name").value(member.getName()));
    }
}