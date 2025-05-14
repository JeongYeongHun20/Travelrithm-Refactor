package com.Travelrithm.repository;

import com.Travelrithm.domain.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {
}
