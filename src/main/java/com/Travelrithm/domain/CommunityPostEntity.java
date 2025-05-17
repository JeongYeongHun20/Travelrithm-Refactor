package com.Travelrithm.domain;

import com.Travelrithm.dto.CommunityPostRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "community_post")
public class CommunityPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<ScrapEntity> scrapEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String title;

    private String postContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity planEntity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isTravelPlan;

    public void update(CommunityPostRequestDto requestDto, PlanEntity planEntity) {
        this.title = requestDto.title();
        this.postContent = requestDto.postContent();
        this.isTravelPlan = requestDto.isTravelPlan();
        this.planEntity = planEntity;
    }

}
