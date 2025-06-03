package com.Travelrithm.planbuilder.publicdata;


import com.Travelrithm.planbuilder.dto.publicdata.DataRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data")
public class PublicController {
    private final PublicService publicService;
    @GetMapping("/places")
    public ResponseEntity<DataResponseDto> getCategory(@RequestBody DataRequestDto dataRequestDto) {
        return ResponseEntity.ok(null);

    }

}
