package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityPostEntity;

import com.Travelrithm.domain.RegionEntity;
import com.Travelrithm.domain.ScrapEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.RegionDto;
import com.Travelrithm.dto.ScrapDto;
import com.Travelrithm.repository.CommunityPostRepository;

import com.Travelrithm.repository.ScrapRepository;
import com.Travelrithm.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;
    private final ScrapRepository scrapRepository;

    public ScrapDto createScrap(Integer userId, Integer postId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        CommunityPostEntity postEntity = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 존재하지 않음"));
        ScrapEntity scrapEntity = ScrapEntity.builder()
                .userEntity(userEntity)
                .postEntity(postEntity)
                .createdAt(LocalDateTime.now())
                .build();

        return new ScrapDto(scrapRepository.save(scrapEntity));
    }
    public void removeScrap(Integer userId, Integer postId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        CommunityPostEntity postEntity = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 존재하지 않음"));
        ScrapEntity scrap = scrapRepository.findByUserEntityAndPostEntity(userEntity,postEntity);
        scrapRepository.delete(scrap);
    }

    public List<ScrapDto> getMyScrap(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 존재하지 않음"));
        List<ScrapEntity> scrapEntity = scrapRepository.findByUserEntity(userEntity);
        return scrapEntity.stream()
                .map(ScrapDto ::new)
                .collect(Collectors.toList());
    }


}
