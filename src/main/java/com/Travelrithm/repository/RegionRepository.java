package com.Travelrithm.repository;

import com.Travelrithm.domain.RegionEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    List<RegionEntity> findByNameContaining(String name);

    List<RegionEntity> findByNameContaining(String name, Sort sort);
}
