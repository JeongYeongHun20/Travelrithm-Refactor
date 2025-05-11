package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Integer> {
    List<CommunityPostEntity> findAllByUserEntity_UserId(Integer userId);
}
