package com.Travelrithm.controller;

import com.Travelrithm.dto.FestivalResponseDto;
import com.Travelrithm.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/festivals")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/calendar")
    public List<FestivalResponseDto> getCalendarEvents() {
        return festivalService.getFestivalEvents();
    }
}
