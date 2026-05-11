package com.Travelrithm.repository;

import com.Travelrithm.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {
    List<CommunityComment> findByCommunityPost_CommunityPostId(Integer communityPostId);

}

