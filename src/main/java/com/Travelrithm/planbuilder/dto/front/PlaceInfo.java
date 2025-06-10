package com.Travelrithm.planbuilder.dto.front;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PlaceInfo {
    Integer day;
    List<Place> places;

    public PlaceInfo(int dayIndex, List<Place> places) {
        this.day = dayIndex;
        this.places = places;
    }

    @Getter
    @Setter
    public static class Place{
        String placeName;
        String url;
        double x,y;

        public Place(String keyword,String url, double x, double y) {
            this.placeName = keyword;
            this.url=url;
            this.x = x;
            this.y = y;
        }
    }
}
