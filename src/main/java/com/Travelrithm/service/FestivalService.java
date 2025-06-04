package com.Travelrithm.service;

import com.Travelrithm.dto.FestivalResponseDto;
import com.Travelrithm.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public List<FestivalResponseDto> getFestivalEvents() {
        return festivalRepository.findAll().stream()
                .map(FestivalResponseDto::fromEntity)
                .toList();
    }
}

