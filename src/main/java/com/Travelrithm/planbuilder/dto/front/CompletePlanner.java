package com.Travelrithm.planbuilder.dto.front;

import com.Travelrithm.planbuilder.dto.publicdata.Item;

import java.util.List;
import java.util.Map;

public record CompletePlanner(
        List<DayMap> dayMapList, //원래 사용자의 장소 list
        Map<Integer, List<Item>> itemList //카테고리에서 추가한 장소들 list
        ) {}
