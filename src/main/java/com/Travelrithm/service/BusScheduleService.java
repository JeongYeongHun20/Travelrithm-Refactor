package com.Travelrithm.service;

import com.Travelrithm.domain.*;
import com.Travelrithm.dto.BusScheduleRequestDto;
import com.Travelrithm.dto.BusScheduleResponseDto;
import com.Travelrithm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusScheduleService {

    private final BusScheduleRepository scheduleRepository;
    private final BusRouteRepository routeRepository;

    @Transactional
    public BusScheduleResponseDto createSchedule(BusScheduleRequestDto dto) {
        BusRouteEntity route = routeRepository.findById(dto.getRouteId()).orElseThrow();

        BusScheduleEntity schedule = BusScheduleEntity.builder()
                .route(route)
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .remainingSeats(dto.getRemainingSeats())
                .createdAt(java.time.LocalDateTime.now())
                .build();

        scheduleRepository.save(schedule);

        return toDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<BusScheduleResponseDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusScheduleResponseDto getScheduleById(Integer id) {
        return toDto(scheduleRepository.findById(id).orElseThrow());
    }

    @Transactional
    public BusScheduleResponseDto updateSchedule(Integer id, BusScheduleRequestDto dto) {
        BusScheduleEntity schedule = scheduleRepository.findById(id).orElseThrow();
        BusRouteEntity route = routeRepository.findById(dto.getRouteId()).orElseThrow();

        schedule.setRoute(route);
        schedule.setDepartureTime(dto.getDepartureTime());
        schedule.setArrivalTime(dto.getArrivalTime());
        schedule.setRemainingSeats(dto.getRemainingSeats());

        return toDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Integer id) {
        scheduleRepository.deleteById(id);
    }

    private BusScheduleResponseDto toDto(BusScheduleEntity s) {
        return BusScheduleResponseDto.builder()
                .scheduleId(s.getScheduleId())
                .routeId(s.getRoute().getRouteId())
                .departureTime(s.getDepartureTime())
                .arrivalTime(s.getArrivalTime())
                .remainingSeats(s.getRemainingSeats())
                .createdAt(s.getCreatedAt())
                .build();
    }
}

