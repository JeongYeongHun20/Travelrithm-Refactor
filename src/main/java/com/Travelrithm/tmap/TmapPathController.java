package com.Travelrithm.tmap;


import com.Travelrithm.tmap.dto.TmapPathRequestDto;
import com.Travelrithm.tmap.dto.TmapPathResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tmap")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TmapPathController {

    private final TmapPathApi tmapPathApi;

    @PostMapping("/routes")
    public ResponseEntity<TmapPathResponseDto> findPath(@RequestBody TmapPathRequestDto tmapPathRequestDto) {
        return ResponseEntity.ok(tmapPathApi.getPath(tmapPathRequestDto));

    }


    @PostMapping("/routes/sub")
    public ResponseEntity<TmapPathResponseDto> findSubPath(@RequestBody TmapPathRequestDto tmapPathRequestDto) {
        return ResponseEntity.ok(null);
    }

}
