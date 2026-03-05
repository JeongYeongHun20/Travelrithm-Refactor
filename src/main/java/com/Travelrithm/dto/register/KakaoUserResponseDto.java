package com.Travelrithm.dto.register;


import com.Travelrithm.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)//역직렬화 시 dto 객체 필드에 없는 json정보 무시
public record KakaoUserResponseDto(
        Long id,
        Boolean has_signed_up,
        Date connected_at,
        KakaoAccount kakao_account
)implements UserRegisterInfo {
    @Override
    public String getName() {
        return kakao_account().name;
    }

    @Override
    public String getEmail() {
        return kakao_account().email();
    }

    @Override
    public String getNickName() {
        return kakao_account().profile().nickname();
    }

    @Override
    public String getSocialId() {
        return String.valueOf(this.id());
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(
        String email,
        String name,
        Profile profile
    ){
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Profile(
            String nickname,
            String thumbnail_image_url,
            String profile_image_url
        ){}

    }


}
