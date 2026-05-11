package com.Travelrithm.repository;

import com.Travelrithm.domain.PlanV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanV2Repository extends JpaRepository<PlanV2,Long> {
}
