package com.engtalkmo.domain.member;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void save() {
        // given
        final String email = "test@engtalkmo.com";
        final String password = "1234567890";
        final String name = "test";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.USER)
                .memberType(MemberType.ENGTALK)
                .profileImage(null)
                .build();

        Member saveMember = memberRepository.save(member);
        close();

        // when
        Member findMember = memberRepository.findById(saveMember.getId())
                .orElse(null);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember).isEqualTo(member);
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getMemberType()).isEqualTo(MemberType.ENGTALK);
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void findByEmail() {
        // given
        final String email = "test@engtalkmo.com";
        final String password = "1234567890";
        final String name = "test";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.USER)
                .memberType(MemberType.ENGTALK)
                .profileImage(null)
                .build();

        Member saveMember = memberRepository.save(member);
        close();

        // when
        Member findMember = memberRepository.findByEmail(email)
                .orElse(null);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember).isEqualTo(member);
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getMemberType()).isEqualTo(MemberType.ENGTALK);
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
    }

    private void close() {
        entityManager.flush();
        entityManager.clear();
    }
}