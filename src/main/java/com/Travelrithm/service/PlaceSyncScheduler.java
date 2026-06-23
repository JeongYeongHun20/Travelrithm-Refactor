package com.Travelrithm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceSyncScheduler {
    private final PlaceSyncService placeSyncService;
    /**서버 옮기거나 초기 데이터 불러올때만 사용**/
//    @EventListener(ApplicationReadyEvent.class)
//    private void syncOnStartup(){
//        log.info("공공데이터 동기화 시작");
//        placeSyncService.syncAllPlaces();
//
//    }
    @Scheduled(cron = "0 0 3 ? * MON", zone = "Asia/Seoul")
    private void syncWeekly() {
        log.info("주간 공공데이터 동기화 시작");
        placeSyncService.syncAllPlaces();
    }


}
