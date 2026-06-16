package com.Travelrithm.planBuilderV2.dto;


import java.util.List;


public record DayMapV2(
        List<SelectedPlace> selectedPlaces,
        int day

) {


}
