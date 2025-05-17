package com.Travelrithm.repository;

import com.Travelrithm.domain.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    Optional<RegionEntity> findByName(String name);
}
