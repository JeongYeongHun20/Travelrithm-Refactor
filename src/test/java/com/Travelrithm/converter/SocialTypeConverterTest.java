package com.Travelrithm.converter;

import com.Travelrithm.domain.SocialType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SocialTypeConverterTest {

    private final SocialTypeConverter socialTypeConverter=new SocialTypeConverter();

    @ParameterizedTest
    @CsvSource({
            "kakao, KAKAO",
            "naver, NAVER",
            "local, LOCAL"
    })
    void convert(String input, SocialType expected) {
        SocialType result=socialTypeConverter.convert(input);
        assertThat(result).isEqualTo(expected);

    }
}