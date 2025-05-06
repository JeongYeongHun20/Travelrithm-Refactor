package com.Travelrithm.service;

import com.Travelrithm.domain.*;
import com.Travelrithm.dto.BusRouteRequestDto;
import com.Travelrithm.dto.BusRouteResponseDto;
import com.Travelrithm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusRouteService {

    private final BusRouteRepository routeRepository;
    private final BusTerminalRepository terminalRepository;
    private final BusCompanyRepository companyRepository;

    @Transactional
    public BusRouteResponseDto createRoute(BusRouteRequestDto dto) {
        BusTerminalEntity departure = terminalRepository.findById(dto.getDepartureTerminalId()).orElseThrow();
        BusTerminalEntity arrival = terminalRepository.findById(dto.getArrivalTerminalId()).orElseThrow();
        BusCompanyEntity company = companyRepository.findById(dto.getCompanyId()).orElseThrow();

        BusRouteEntity route = BusRouteEntity.builder()
                .departureTerminal(departure)
                .arrivalTerminal(arrival)
                .company(company)
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .build();

        routeRepository.save(route);

        return toDto(route);
    }

    @Transactional(readOnly = true)
    public List<BusRouteResponseDto> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusRouteResponseDto getRouteById(Integer id) {
        return toDto(routeRepository.findById(id).orElseThrow());
    }

    @Transactional
    public BusRouteResponseDto updateRoute(Integer id, BusRouteRequestDto dto) {
        BusRouteEntity route = routeRepository.findById(id).orElseThrow();
        BusTerminalEntity departure = terminalRepository.findById(dto.getDepartureTerminalId()).orElseThrow();
        BusTerminalEntity arrival = terminalRepository.findById(dto.getArrivalTerminalId()).orElseThrow();
        BusCompanyEntity company = companyRepository.findById(dto.getCompanyId()).orElseThrow();

        route.update(departure, arrival, company, dto.getDuration(), dto.getPrice());
        return toDto(route);
    }

    @Transactional
    public void deleteRoute(Integer id) {
        routeRepository.deleteById(id);
    }

    private BusRouteResponseDto toDto(BusRouteEntity route) {
        return BusRouteResponseDto.builder()
                .routeId(route.getRouteId())
                .departureTerminalId(route.getDepartureTerminal().getTerminalId())
                .arrivalTerminalId(route.getArrivalTerminal().getTerminalId())
                .companyId(route.getCompany().getCompanyId())
                .duration(route.getDuration())
                .price(route.getPrice())
                .createdAt(route.getCreatedAt())
                .build();
    }
}


