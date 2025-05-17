package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Integer> {
    List<CommunityPostEntity> findAllByUserEntity_UserId(Integer userId);
    Page<CommunityPostEntity> findAllByIsTravelPlanTrue(Pageable pageable);   // 플랜게시판
    Page<CommunityPostEntity> findAllByIsTravelPlanFalse(Pageable pageable);  // 자유게시판
}
