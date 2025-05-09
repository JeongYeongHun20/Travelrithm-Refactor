package com.Travelrithm.repository;

import com.Travelrithm.domain.BusScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusScheduleRepository extends JpaRepository<BusScheduleEntity, Integer> {
}