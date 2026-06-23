package com.Travelrithm.publicdata.v3;

import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.publicdata.v2.dto.AreaBasedResponse;
import com.Travelrithm.publicdata.v2.dto.DetailCommon;
import com.Travelrithm.publicdata.v2.dto.LocationBasedListResponse;
import com.Travelrithm.publicdata.v3.annotation.PublicDataRestClient;
import com.Travelrithm.publicdata.v3.properties.PublicDataProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@Slf4j
@Component
public class PublicDataClient {
    private final RestClient restClient;
    private final PublicDataProperties properties;
    public PublicDataClient(
            @PublicDataRestClient RestClient restClient,
            PublicDataProperties properties
    ){
        this.restClient=restClient;
        this.properties=properties;
    }

    public LocationBasedListResponse fetchLocationBasedList(AvgCoordinate avgCoordinate, String cat1, String cat2){
        return restClient
                .get()
                .uri(uriBuilder -> {
                    URI uri=initParameter(
                            uriBuilder
                                .path(properties.locationBasedList2())
                                .queryParam("mapX", avgCoordinate.location().x())
                                .queryParam("mapY", avgCoordinate.location().y())
                                .queryParam("radius", String.valueOf((int) avgCoordinate.radius()))
                                .queryParam("cat1", cat1)
                                .queryParam("cat2", cat2)
                    ).build(true);
                    log.info(maskServiceKey(uri.toString()));
                    return uri;
                })
                .retrieve()
                .body(LocationBasedListResponse.class);

    }
    public DetailCommon fetchDetailCommon(String contentId){
        return restClient
                .get()
                .uri(uriBuilder -> initParameter(
                        uriBuilder
                                .path(properties.detailCommon2())
                                .queryParam("contentId", contentId)
                ).build())
                .retrieve()
                .body(DetailCommon.class);

    }
    public AreaBasedResponse fetchAreaBasedSyncList(int pageNo, int numOfRows){
        return restClient
                .get()
                .uri(uriBuilder -> initParameter(
                        uriBuilder
                                .path(properties.areaBasedList2())
                                .queryParam("pageNo", pageNo)
                                .queryParam("numOfRows", numOfRows)

                ).build())
                .retrieve()
                .body(AreaBasedResponse.class);
    }

    private UriBuilder initParameter(UriBuilder uriBuilder){
        return uriBuilder
                .queryParam("MobileOS",properties.mobileOs())
                .queryParam("MobileApp", properties.mobileApp())
                .queryParam("serviceKey", properties.serviceKey())
                .queryParam("_type", "json");

    }


    private String maskServiceKey(String uri) {
        if (uri == null || properties.serviceKey() == null || properties.serviceKey().isBlank()) {
            return uri;
        }
        return uri.replace(properties.serviceKey(), "****");
    }


}
