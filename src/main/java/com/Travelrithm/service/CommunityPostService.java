package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.PlanEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.CommunityPostRequestDto;
import com.Travelrithm.dto.CommunityPostResponseDto;
import com.Travelrithm.dto.PlaceDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.repository.CommunityPostRepository;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public CommunityPostResponseDto createPost(Integer userId, CommunityPostRequestDto postRequestDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다"));
        PlanEntity planEntity = null;
        if (postRequestDto.isTravelPlan() && postRequestDto.planId() != null) {
            planEntity = planRepository.findById(postRequestDto.planId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 플랜이 존재하지 않습니다"));
        }
        CommunityPostEntity postEntity = CommunityPostEntity.builder()
                .userEntity(userEntity)
                .planEntity(planEntity)
                .title(postRequestDto.title())
                .postContent(postRequestDto.postContent())
                .isTravelPlan(postRequestDto.isTravelPlan())
                .build();
        CommunityPostEntity saved = postRepository.save(postEntity);
        return CommunityPostResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<CommunityPostResponseDto> getAllPosts(Integer userId) {
        return postRepository.findAllByUserEntity_UserId(userId)
                .stream()
                .map(CommunityPostResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunityPostResponseDto getPost(Integer postId) {
        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));

        if (!post.getIsTravelPlan()) {
            post.setViewCount(post.getViewCount() + 1);
        }

        return CommunityPostResponseDto.fromEntity(post);
    }

    public CommunityPostResponseDto updatePost(Integer postId, CommunityPostRequestDto postRequestDto) {
        CommunityPostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        PlanEntity planEntity = planRepository.findById(postRequestDto.planId())
                .orElseThrow(() -> new IllegalArgumentException("해당 플랜이 존재하지 않음"));
        postEntity.update(postRequestDto, planEntity);
        return CommunityPostResponseDto.fromEntity(postEntity);
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    public PlanEntity getPopularPlanByRegion(Integer regionId) {
        List<PlanEntity> plans = postRepository.findTopPopularPlanByRegion(regionId, PageRequest.of(0, 1));
        return plans.isEmpty() ? null : plans.get(0);
    }

    public List<CommunityPostResponseDto> getPlanPosts(int page) {
        Page<CommunityPostEntity> postPage = postRepository.findAllByIsTravelPlanTrue(PageRequest.of(page, 10));

        return postPage.stream().map(post -> {
            PlanEntity plan = post.getPlanEntity();
            if (plan == null) return null;

            String regionThumbnail = (plan.getRegionEntity() != null)
                    ? plan.getRegionEntity().getThumbnailImageUrl()
                    : null;

            PlanResponseDto planDto = new PlanResponseDto(plan);

            List<PlaceDto> places = plan.getPlaceEntities().stream()
                    .map(PlaceDto::new)
                    .toList();

            // 지역 기반 인기 플랜 조회
            PlanEntity popular = getPopularPlanByRegion(plan.getRegionEntity().getRegionId());
            PlanResponseDto popularDto = new PlanResponseDto(popular);

            return new CommunityPostResponseDto(
                    post.getPostId(),
                    post.getUserEntity().getUserId(),
                    post.getTitle(),
                    post.getPostContent(),
                    post.getIsTravelPlan(),
                    plan.getPlanId(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    regionThumbnail,
                    post.getUserEntity().getNickname(),
                    planDto,
                    places,
                    popularDto,
                    post.getViewCount(),
                    post.getScrapEntities().size(),
                    post.getCommentEntities().size(),
                    (plan.getRegionEntity() != null) ? plan.getRegionEntity().getName() : null
            );
        }).filter(dto -> dto != null).toList();
    }


    public List<CommunityPostResponseDto> getFreePosts(int page) {
        Page<CommunityPostEntity> postPage = postRepository.findAllByIsTravelPlanFalse(PageRequest.of(page, 10));

        return postPage.stream()
                .map(CommunityPostResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void increaseViewCount(Integer postId) {
        CommunityPostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        post.increaseViewCount();
    }
}

