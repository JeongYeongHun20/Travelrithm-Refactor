package com.Travelrithm.planBuilderV2;

import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.planBuilderV2.dto.CalculateRequestDto;
import com.Travelrithm.planBuilderV2.dto.LocationV2;
import com.Travelrithm.planbuilder.dto.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CenterLocationCalculator {
    private final int R = 6371000; // 지구 반지름 (단위: m)

    public AvgCoordinate calLocation(CalculateRequestDto calculateRequestDto) {
        int day=calculateRequestDto.day();
        List<LocationV2> locations=calculateRequestDto.locations();

        if (locations == null || locations.isEmpty()) {
            System.out.println("위치 정보가 없습니다.");
            return null;
        }
        for(LocationV2 location: locations){
            log.info(location.x()+" "+location.y());
        }

        double sumLat = 0, sumLon = 0;
        for (LocationV2 loc : locations) {
            sumLat += loc.y();  // 위도
            sumLon += loc.x();  // 경도
        }
        double avgLat = sumLat / locations.size();
        double avgLon = sumLon / locations.size();

        double totalDistance = 0;
        for (LocationV2 loc : locations) {
            double lat2=loc.y();
            double lon2=loc.x();
            double dLat = Math.toRadians(lat2 - avgLat);
            double dLon = Math.toRadians(lon2 - avgLon);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(avgLat)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            totalDistance+= R * c;
        }


        double avgRadius = totalDistance / locations.size();

        System.out.printf("중심점 위도: %.6f, 경도: %.6f\n", avgLat, avgLon);
        System.out.printf("평균 반지름 (좌표 기준): %.6f\n", avgRadius);

        return new AvgCoordinate(day,new Location(avgLon,avgLat), avgRadius);
    }


}
