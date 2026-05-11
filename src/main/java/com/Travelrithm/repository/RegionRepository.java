package com.Travelrithm.repository;

import com.Travelrithm.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RegionRepository extends JpaRepository<Region, Integer> {
    List<Region> findBySigunguNameStartingWith(String sigunguName);
}
