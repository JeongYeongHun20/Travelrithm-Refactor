package com.Travelrithm.repository.PlaceRepo;


import com.Travelrithm.publicdata.v2.dto.AreaBasedResponseItem;

import java.util.List;

public interface PlaceRepositoryCustom {
    void bulkUpsert(List<AreaBasedResponseItem> items);
}
