package com.Travelrithm.repository;

import com.Travelrithm.domain.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
}
