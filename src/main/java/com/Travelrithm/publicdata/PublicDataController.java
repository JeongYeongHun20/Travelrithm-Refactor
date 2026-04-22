package com.Travelrithm.publicdata;


import com.Travelrithm.publicdata.dto.DataRequestDto;
import com.Travelrithm.publicdata.dto.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class PublicDataController {
    private final PublicDataApi publicDataApi;
    @GetMapping("/places")
    public ResponseEntity<DataResponseDto> getCategory(@RequestBody DataRequestDto dataRequestDto) {
        return ResponseEntity.ok(null);

    }

}
