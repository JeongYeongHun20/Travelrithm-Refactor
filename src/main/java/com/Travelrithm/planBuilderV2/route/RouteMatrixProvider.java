package com.Travelrithm.planBuilderV2.route;

import com.Travelrithm.planBuilderV2.dto.DayMapV2;

import java.util.List;

public interface RouteMatrixProvider {
    RouteEdge[][] create(List<DayMapV2.Content> contents);
}
