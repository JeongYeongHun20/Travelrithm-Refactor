package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityPost;

import com.Travelrithm.domain.Scrap;
import com.Travelrithm.domain.Member;
import com.Travelrithm.dto.ScrapDto;
import com.Travelrithm.repository.CommunityPostRepository;

import com.Travelrithm.repository.ScrapRepository;
import com.Travelrithm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {
    private final MemberRepository memberRepository;
    private final CommunityPostRepository communityPostRepository;
    private final ScrapRepository scrapRepository;

    @Transactional
    public ScrapDto createScrap(Long memberId, Integer postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 존재하지 않음"));
        Scrap existing = scrapRepository.findByMemberAndPost(member, post);

        if (existing != null) {
            scrapRepository.delete(existing);
            return null; // scrap 있으면 기존 거 삭제
        } else {
            Scrap newScrap = Scrap.builder()
                    .member(member)
                    .post(post)
                    .createdAt(LocalDateTime.now())
                    .build();
            return new ScrapDto(scrapRepository.save(newScrap)); // 없으면 생성
        }
    }

    public void removeScrap(Long memberId, Integer postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        CommunityPost postEntity = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 존재하지 않음"));
        Scrap scrap = scrapRepository.findByMemberAndPost(member,postEntity);
        scrapRepository.delete(scrap);
    }

    public List<ScrapDto> getMyScrap(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        List<Scrap> scrap = scrapRepository.findByMember(member);
        return scrap.stream()
                .map(ScrapDto ::new)
                .collect(Collectors.toList());
    }



}
