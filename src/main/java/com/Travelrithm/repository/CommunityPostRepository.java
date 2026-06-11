package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityPost;
import com.Travelrithm.domain.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Integer> {

    List<CommunityPost> findAllByMember_Id(Long memberId);

    Page<CommunityPost> findAllByIsTravelPlanTrue(Pageable pageable);   // 플랜게시판
    Page<CommunityPost> findAllByIsTravelPlanFalse(Pageable pageable);  // 자유게시판

    List<CommunityPost> findAllByMember_IdAndIsTravelPlanTrue(Long memberId);
    List<CommunityPost> findByPlan(Plan plan);


    // 인기 plan 조회 (region 기준)
    @Query("""
        SELECT p
        FROM CommunityPost c
        JOIN c.plan p
        WHERE p.region.sigunguCd = :sigunguCd
        GROUP BY p
        ORDER BY COUNT(c) DESC
    """)
    List<Plan> findTopPopularPlanByRegion(@Param("regionId") String sigunguCd, Pageable pageable);
}

