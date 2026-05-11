package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;

import java.util.List;

public interface Generator {
    List<SortedDayPlan> generatePlan(PlanGenerateRequest planGenerateRequest);

}
