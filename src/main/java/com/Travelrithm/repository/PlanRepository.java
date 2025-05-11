package com.Travelrithm.repository;

import com.Travelrithm.domain.PlanEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.RegionCountDto;
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
        SELECT new com.Travelrithm.dto.RegionCountDto(r.regionId, r.name, r.context,COUNT(p))
        FROM PlanEntity p
        JOIN p.regionEntity r
        GROUP BY r.regionId, r.name
        ORDER BY COUNT(p) DESC
        LIMIT 5
    """)
    List<RegionCountDto> findPopularRegions();

}
