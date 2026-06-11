package com.Travelrithm.repository;

import com.Travelrithm.domain.Member;
import com.Travelrithm.domain.PlanV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanV2Repository extends JpaRepository<PlanV2,Long> {

    List<PlanV2> findAllByMember_Id(Long memberId);

}
