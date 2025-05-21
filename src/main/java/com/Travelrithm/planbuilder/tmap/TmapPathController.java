package com.Travelrithm.planbuilder.tmap;


import com.Travelrithm.planbuilder.dto.tmap.TmapPathRequestDto;
import com.Travelrithm.planbuilder.dto.tmap.TmapPathResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tmap")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TmapPathController {

    private final TmapPathService tmapPathService;

    @PostMapping("/routes")
    public ResponseEntity<TmapPathResponseDto> findPath(@RequestBody TmapPathRequestDto tmapPathRequestDto) {
        return ResponseEntity.ok(tmapPathService.getPath(tmapPathRequestDto));

    }


    @PostMapping("/routes/sub")
    public ResponseEntity<TmapPathResponseDto> findSubPath(@RequestBody TmapPathRequestDto tmapPathRequestDto) {
        return ResponseEntity.ok(null);
    }

}
