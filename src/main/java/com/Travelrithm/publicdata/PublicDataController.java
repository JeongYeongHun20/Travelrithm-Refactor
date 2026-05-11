package com.Travelrithm.publicdata;


import com.Travelrithm.publicdata.dto.DataRequestDto;
import com.Travelrithm.publicdata.dto.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class PublicDataController {
    private final PublicDataApiV2 publicDataApi;
    @GetMapping("/places")
    public ResponseEntity<DataResponseDto> getCategory(@RequestBody DataRequestDto dataRequestDto) {
        return ResponseEntity.ok(null);

    }

    @GetMapping("/places/{contentId}/overview")
    public ResponseEntity<String> getOverview(@PathVariable String contentId){
        String overView = publicDataApi.getOverView(contentId);
        return ResponseEntity.ok(overView);
    }

}
