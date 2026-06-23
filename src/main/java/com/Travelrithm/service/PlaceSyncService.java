package com.Travelrithm.service;

import com.Travelrithm.publicdata.v2.dto.AreaBasedResponse;
import com.Travelrithm.publicdata.v2.dto.AreaBasedResponseItem;
import com.Travelrithm.publicdata.v3.PublicDataClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceSyncService {
    private final PublicDataClient publicDataClient;
    private final PlaceService placeService;
    private final static int NUM_OF_ROWS = 1000;

    @Transactional
    public void syncAllPlaces() {
        long start = System.currentTimeMillis();
        int pageNo = 1;
        int totalSavedCount=0;
        while (true) {
            long pageStart = System.currentTimeMillis();
            AreaBasedResponse response =
                    publicDataClient.fetchAreaBasedSyncList(pageNo, NUM_OF_ROWS);

            List<AreaBasedResponseItem> items = response.toItems();

            if (items.isEmpty()) {
                log.info("공공데이터 조회 결과 없음. pageNo={}", pageNo);
                break;
            }

            placeService.upsertPlaces(items);
            totalSavedCount += items.size();
            long pageEnd = System.currentTimeMillis();

            log.info("공공데이터 동기화 pageNo={}, size={}, pageTime={}ms, totalCount={}",
                    pageNo,
                    items.size(),
                    pageEnd - pageStart,
                    response.totalCount()
            );

            if (pageNo * NUM_OF_ROWS >= response.totalCount()) {
                break;
            }

            pageNo++;
        }
        long end = System.currentTimeMillis();
        log.info("공공데이터 전체 동기화 완료. totalSavedCount={}, totalTime={}ms, totalTimeSec={}초",
                totalSavedCount,
                end - start,
                (end - start) / 1000
        );
    }

}