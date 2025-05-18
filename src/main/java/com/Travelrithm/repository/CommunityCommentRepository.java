package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityCommentEntity, Integer> {
    List<CommunityCommentEntity> findByPostEntity_PostId(Integer postId);

}

