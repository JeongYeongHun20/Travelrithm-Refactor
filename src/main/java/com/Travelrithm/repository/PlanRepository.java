package com.Travelrithm.repository;

import com.Travelrithm.domain.Member;
import com.Travelrithm.domain.Plan;
import com.Travelrithm.dto.RegionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findAllByMember_MemberId(Long memberId);

    //상위 5개 데이터만 불러옴
    @Query("""
        SELECT new com.Travelrithm.dto.RegionDto(r.sigunguCd, r.sigunguName, r.areaCd, r.areaName)
        FROM com.Travelrithm.domain.Plan p
        JOIN p.region r
        GROUP BY r.sigunguCd, r.sigunguName
        ORDER BY COUNT(p) DESC
        LIMIT 5
    """)
    List<RegionDto> findPopularRegions();

    Long member(Member member);
}
