package com.Travelrithm.controller;

import com.Travelrithm.dto.BusReservRequestDto;
import com.Travelrithm.dto.BusReservResponseDto;
import com.Travelrithm.service.BusReservService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus-reserv")
@RequiredArgsConstructor
public class BusReservController {

    private final BusReservService busReservService;

    @PostMapping
    public BusReservResponseDto create(@RequestBody BusReservRequestDto dto) {
        return busReservService.createReservation(dto);
    }

    @GetMapping
    public List<BusReservResponseDto> getAll() {
        return busReservService.getAllReservations();
    }

    @GetMapping("/{id}")
    public BusReservResponseDto getById(@PathVariable Integer id) {
        return busReservService.getReservationById(id);
    }

    @PutMapping("/{id}")
    public BusReservResponseDto update(@PathVariable Integer id, @RequestBody BusReservRequestDto dto) {
        return busReservService.updateReservation(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        busReservService.deleteReservation(id);
    }
}


