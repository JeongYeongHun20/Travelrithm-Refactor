package com.Travelrithm.repository;

import com.Travelrithm.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
        Member findByName(String name);
        Optional<Member> findByEmail(String email);

        Long memberId(Long memberId);
}
