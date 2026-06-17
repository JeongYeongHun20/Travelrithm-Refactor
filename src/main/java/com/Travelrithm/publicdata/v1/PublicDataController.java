package com.Travelrithm.publicdata.v1;


import com.Travelrithm.publicdata.v1.dto.DataRequestDto;
import com.Travelrithm.publicdata.v1.dto.DataResponseDto;
import com.Travelrithm.publicdata.v2.PublicDataServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class PublicDataController {
    private final PublicDataServiceV2 publicDataApi;
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
