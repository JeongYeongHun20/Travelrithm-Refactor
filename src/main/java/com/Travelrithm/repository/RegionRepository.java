package com.Travelrithm.repository;

import com.Travelrithm.domain.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    List<RegionEntity> findBySigunguNameStartingWith(String sigunguName);
}
