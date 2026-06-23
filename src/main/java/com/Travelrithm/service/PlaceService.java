package com.Travelrithm.service;

import com.Travelrithm.publicdata.v2.dto.AreaBasedResponseItem;
import com.Travelrithm.repository.PlaceRepo.PlaceV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceV2Repository placeRepository;

    public void upsertPlaces(List<AreaBasedResponseItem> items){
        placeRepository.bulkUpsert(items);
    }
}
