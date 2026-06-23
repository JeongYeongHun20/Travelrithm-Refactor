package com.Travelrithm.publicdata;

import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.publicdata.v2.dto.RegionLocationResponse;

import java.util.List;

public interface PublicDataApi {
    RegionLocationResponse getCategory(List<AvgCoordinate> avgCoordinates, String preference);
    String getOverView(String contentId);
}
