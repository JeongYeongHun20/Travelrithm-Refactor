package com.Travelrithm.repository;


import com.Travelrithm.domain.CommunityPost;
import com.Travelrithm.domain.Scrap;
import com.Travelrithm.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    Scrap findByMemberAndPost(Member member, CommunityPost post);

    List<Scrap> findByMember(Member member);
}