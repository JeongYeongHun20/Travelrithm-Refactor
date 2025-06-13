package com.Travelrithm.domain;


import com.Travelrithm.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)// jpa사용하기위하여, Builder로만 접근 가능
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private String nickname;

    private String role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime nicknameUpdatedAt;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PlanEntity> planEntities = new ArrayList<>();

    public void update(UserRequestDto dto) {
        this.name = dto.name();
        this.password=dto.password();
        this.email = dto.email();
        this.nickname = dto.nickname();
    }




}
