package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.PlanEntity;
import com.Travelrithm.dto.RegionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Integer> {
    List<PlanEntity> findAllByUserEntity_UserId(Integer userId);

    //상위 5개 데이터만 불러옴
    @Query("""
        SELECT new com.Travelrithm.dto.RegionDto(r.regionId, r.name, r.context,COUNT(p),r.thumbnailImageUrl, r.code)
        FROM PlanEntity p
        JOIN p.regionEntity r
        GROUP BY r.regionId, r.name, r.code
        ORDER BY COUNT(p) DESC
        LIMIT 5
    """)
    List<RegionDto> findPopularRegions();

}
