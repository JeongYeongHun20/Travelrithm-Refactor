package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;

import java.util.List;

public interface RouteSequencer {
    List<SortedDayPlan> sequence(List<DayMapV2> dayMapV2s);

}
