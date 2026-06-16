package com.Travelrithm.planBuilderV2.route;

import com.Travelrithm.planBuilderV2.dto.SelectedPlace;

import java.util.List;

public interface RouteMatrixProvider {
    RouteEdge[][] create(List<SelectedPlace> selectedPlaces);
}
