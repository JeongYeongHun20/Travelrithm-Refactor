package com.Travelrithm.converter;

import com.Travelrithm.domain.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocialTypeConverter implements Converter<String, SocialType> {

    @Override
    public SocialType convert(String source) {
        log.info(source);
        return SocialType.fromName(source);
    }
}
