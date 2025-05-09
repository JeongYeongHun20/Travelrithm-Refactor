package com.Travelrithm.service;

import com.Travelrithm.domain.*;
import com.Travelrithm.dto.BusReservRequestDto;
import com.Travelrithm.dto.BusReservResponseDto;
import com.Travelrithm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusReservService {

    private final BusReservRepository busReservRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final BusTerminalRepository terminalRepository;

    //CRUD
    @Transactional
    public BusReservResponseDto createReservation(BusReservRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow();
        PlanEntity plan = planRepository.findById(dto.getPlanId()).orElseThrow();
        BusTerminalEntity departure = terminalRepository.findById(dto.getDepartureTerminalId()).orElseThrow();
        BusTerminalEntity arrival = terminalRepository.findById(dto.getArrivalTerminalId()).orElseThrow();

        BusReservEntity entity = BusReservEntity.builder()
                .user(user)
                .plan(plan)
                .direction(dto.getDirection())
                .departureTerminal(departure)
                .arrivalTerminal(arrival)
                .departureTime(dto.getDepartureTime())
                .seatNumber(dto.getSeatNumber())
                .seatStatus(dto.getSeatStatus())
                .build();

        BusReservEntity saved = busReservRepository.save(entity);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BusReservResponseDto> getAllReservations() {
        return busReservRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusReservResponseDto getReservationById(Integer id) {
        BusReservEntity entity = busReservRepository.findById(id).orElseThrow();
        return toDto(entity);
    }

    @Transactional
    public BusReservResponseDto updateReservation(Integer id, BusReservRequestDto dto) {
        BusReservEntity entity = busReservRepository.findById(id).orElseThrow();
        entity.setDirection(dto.getDirection());
        entity.setDepartureTerminal(terminalRepository.findById(dto.getDepartureTerminalId()).orElseThrow());
        entity.setArrivalTerminal(terminalRepository.findById(dto.getArrivalTerminalId()).orElseThrow());
        entity.setDepartureTime(dto.getDepartureTime());
        entity.setSeatNumber(dto.getSeatNumber());
        entity.setSeatStatus(dto.getSeatStatus());
        return toDto(entity);
    }

    @Transactional
    public void deleteReservation(Integer id) {
        busReservRepository.deleteById(id);
    }

    private BusReservResponseDto toDto(BusReservEntity r) {
        return BusReservResponseDto.builder()
                .reservationId(r.getReservationId())
                .userId(r.getUser().getUserId())
                .planId(r.getPlan().getPlanId())
                .direction(r.getDirection())
                .departureTerminalId(r.getDepartureTerminal().getTerminalId())
                .arrivalTerminalId(r.getArrivalTerminal().getTerminalId())
                .departureTime(r.getDepartureTime())
                .seatNumber(r.getSeatNumber())
                .seatStatus(r.getSeatStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
