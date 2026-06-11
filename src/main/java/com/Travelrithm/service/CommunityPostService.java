package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityPost;
import com.Travelrithm.domain.Plan;
import com.Travelrithm.domain.Member;
import com.Travelrithm.dto.CommunityPostRequestDto;
import com.Travelrithm.dto.CommunityPostResponseDto;
import com.Travelrithm.dto.PlaceDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.repository.CommunityPostRepository;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    public CommunityPostResponseDto createPost(Long userId, CommunityPostRequestDto postRequestDto) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다"));
        Plan plan = null;
        if (postRequestDto.isTravelPlan() && postRequestDto.planId() != null) {
            plan = planRepository.findById(postRequestDto.planId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 플랜이 존재하지 않습니다"));
        }
        CommunityPost postEntity = CommunityPost.builder()
                .member(member)
                .plan(plan)
                .title(postRequestDto.title())
                .postContent(postRequestDto.postContent())
                .isTravelPlan(postRequestDto.isTravelPlan())
                .build();
        CommunityPost saved = postRepository.save(postEntity);
        return CommunityPostResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<CommunityPostResponseDto> getAllPosts(Long userId) {
        return postRepository.findAllByMember_Id(userId)
                .stream()
                .map(CommunityPostResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunityPostResponseDto getPost(Integer postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));

        if (!post.getIsTravelPlan()) {
            post.setViewCount(post.getViewCount() + 1);
        }

        return CommunityPostResponseDto.fromEntity(post);
    }

    public CommunityPostResponseDto updatePost(Integer postId, CommunityPostRequestDto postRequestDto) {
        CommunityPost postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Plan plan = planRepository.findById(postRequestDto.planId())
                .orElseThrow(() -> new IllegalArgumentException("해당 플랜이 존재하지 않음"));
        postEntity.update(postRequestDto, plan);
        return CommunityPostResponseDto.fromEntity(postEntity);
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    public Plan getPopularPlanByRegion(String sigunguCd) {
        List<Plan> plans = postRepository.findTopPopularPlanByRegion(sigunguCd, PageRequest.of(0, 1));
        return plans.isEmpty() ? null : plans.get(0);
    }

    public List<CommunityPostResponseDto> getPlanPosts(int page) {
        Page<CommunityPost> postPage = postRepository.findAllByIsTravelPlanTrue(PageRequest.of(page, 10));

        return postPage.stream().map(post -> {
            Plan plan = post.getPlan();
            if (plan == null) return null;

            PlanResponseDto planDto = new PlanResponseDto(plan, null);

            List<PlaceDto> places = plan.getPlaceEntities().stream()
                    .map(PlaceDto::new)
                    .toList();

            // 지역 기반 인기 플랜 조회
            Plan popular = getPopularPlanByRegion(plan.getRegion().getSigunguCd());
            PlanResponseDto popularDto = new PlanResponseDto(popular, null);

            return new CommunityPostResponseDto(
                    post.getId(),
                    post.getMember().getId(),
                    post.getTitle(),
                    post.getPostContent(),
                    post.getIsTravelPlan(),
                    plan.getId(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    post.getMember().getNickname(),
                    planDto,
                    places,
                    popularDto,
                    post.getViewCount(),
                    post.getScraps().size(),
                    post.getComments().size(),
                    plan.getRegion().getSigunguName()
            );
        }).filter(dto -> dto != null).toList();
    }


    public List<CommunityPostResponseDto> getFreePosts(int page) {
        Page<CommunityPost> postPage = postRepository.findAllByIsTravelPlanFalse(PageRequest.of(page, 10));

        return postPage.stream()
                .map(CommunityPostResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void increaseViewCount(Integer postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        post.increaseViewCount();
    }

    @Transactional(readOnly = true)
    public List<CommunityPostResponseDto> getMyPlanPosts(Long userId) {
        return postRepository.findAllByMember_IdAndIsTravelPlanTrue(userId).stream()
                .map(CommunityPostResponseDto::fromEntity)
                .toList();
    }

}

