package com.Travelrithm.repository;

import com.Travelrithm.domain.BusReservEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusReservRepository extends JpaRepository<BusReservEntity, Integer> {
}
