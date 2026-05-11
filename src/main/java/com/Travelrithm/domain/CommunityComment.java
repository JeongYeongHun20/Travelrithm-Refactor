package com.Travelrithm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communityPost_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CommunityPost communityPost;


    @Column(name = "comment_content", columnDefinition = "TEXT")
    private String commentContent;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String commentContent) {
        this.commentContent = commentContent;
    }
}
