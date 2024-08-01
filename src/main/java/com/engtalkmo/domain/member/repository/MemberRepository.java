package com.engtalkmo.domain.member.repository;

import com.engtalkmo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
