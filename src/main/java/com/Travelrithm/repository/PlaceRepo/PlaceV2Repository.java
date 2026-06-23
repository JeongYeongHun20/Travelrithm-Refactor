package com.Travelrithm.repository.PlaceRepo;

import com.Travelrithm.domain.PlaceV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceV2Repository extends JpaRepository<PlaceV2, Long>, PlaceRepositoryCustom {

}
