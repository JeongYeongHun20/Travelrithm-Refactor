package com.Travelrithm.controller;

import com.Travelrithm.dto.CommunityPostRequestDto;
import com.Travelrithm.dto.CommunityPostResponseDto;
import com.Travelrithm.dto.ScrapDto;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.service.CommunityPostService;
import com.Travelrithm.service.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService postService;
    private final ScrapService scrapService;
    private final CommunityPostService communityPostService;

    @PostMapping("/createPost")
    public CommunityPostResponseDto createPost(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestBody CommunityPostRequestDto request) {
        Integer userId = userDetails.getUserId();
        return postService.createPost(userId, request);
    }

    @GetMapping("/getPosts")
    public List<CommunityPostResponseDto> getPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        return postService.getAllPosts(userId);
    }

    @GetMapping("/{postId}")
    public CommunityPostResponseDto getPost(@PathVariable(name = "postId") Integer postId) {
        return postService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public CommunityPostResponseDto update(@PathVariable Integer postId, @RequestBody CommunityPostRequestDto postRequestDto) {
        return postService.updatePost(postId, postRequestDto);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Integer postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/{postId}/scrap")
    public ScrapDto toggleScrap(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "postId") Integer postId) {
        Integer userId=userDetails.getUserId();
        return scrapService.createScrap(userId, postId);

    }
    @DeleteMapping("/{postId}/scrap")
    public ResponseEntity<Void> unToggleScrap(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "postId") Integer postId) {
        Integer userId=userDetails.getUserId();
        scrapService.removeScrap(userId,postId);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/plan")
    public ResponseEntity<List<CommunityPostResponseDto>> getPlanPosts(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(communityPostService.getPlanPosts(page));
    }

    @GetMapping("/free")
    public ResponseEntity<List<CommunityPostResponseDto>> getFreePosts(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(communityPostService.getFreePosts(page));
    }
}
