package com.Travelrithm.domain;

import com.Travelrithm.dto.CommunityPostRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityPostId;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Scrap> scrapEntities = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    private String title;

    private String postContent;

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CommunityComment> commentEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Plan plan;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isTravelPlan;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void update(CommunityPostRequestDto requestDto, Plan plan) {
        this.title = requestDto.title();
        this.postContent = requestDto.postContent();
        this.isTravelPlan = requestDto.isTravelPlan();
        this.plan = plan;
    }

}
