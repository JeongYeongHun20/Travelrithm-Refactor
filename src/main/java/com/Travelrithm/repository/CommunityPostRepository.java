package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.PlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Integer> {

    List<CommunityPostEntity> findAllByUserEntity_UserId(Integer userId);

    Page<CommunityPostEntity> findAllByIsTravelPlanTrue(Pageable pageable);   // 플랜게시판
    Page<CommunityPostEntity> findAllByIsTravelPlanFalse(Pageable pageable);  // 자유게시판

    List<CommunityPostEntity> findAllByUserEntity_UserIdAndIsTravelPlanTrue(Integer userId);


    // 인기 plan 조회 (region 기준)
    @Query("""
        SELECT p
        FROM CommunityPostEntity c
        JOIN c.planEntity p
        WHERE p.regionEntity.regionId = :regionId
        GROUP BY p
        ORDER BY COUNT(c) DESC
    """)
    List<PlanEntity> findTopPopularPlanByRegion(@Param("regionId") Integer regionId, Pageable pageable);
}

