package com.Travelrithm.repository.PlaceRepo;


import com.Travelrithm.publicdata.v2.dto.AreaBasedResponseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void bulkUpsert(List<AreaBasedResponseItem> items) {
        String sql = """
                insert into place_v2 (
                    content_id,
                    content_type_id,
                    title,
                    addr1,
                    addr2,
                    zipcode,
                    longitude,
                    latitude,
                    first_image,
                    thumbnail_image,
                    modified_time,
                    legal_dong_region_code,
                    legal_dong_sigungu_code,
                    category1,
                    category2,
                    category3
                )
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                on duplicate key update
                    content_type_id = values(content_type_id),
                    title = values(title),
                    addr1 = values(addr1),
                    addr2 = values(addr2),
                    zipcode = values(zipcode),
                    longitude = values(longitude),
                    latitude = values(latitude),
                    first_image = values(first_image),
                    thumbnail_image = values(thumbnail_image),
                    modified_time = values(modified_time),
                    legal_dong_region_code = values(legal_dong_region_code),
                    legal_dong_sigungu_code = values(legal_dong_sigungu_code),
                    category1 = values(category1),
                    category2 = values(category2),
                    category3 = values(category3)
                """;

        jdbcTemplate.batchUpdate(sql, items, 1000, (ps, item) -> {
            ps.setLong(1, item.contentId());
            ps.setString(2, item.contentTypeId());
            ps.setString(3, item.title());
            ps.setString(4, item.addr1());
            ps.setString(5, item.addr2());
            ps.setString(6, item.zipcode());
            ps.setBigDecimal(7, item.longitude());
            ps.setBigDecimal(8, item.latitude());
            ps.setString(9, item.firstImage());
            ps.setString(10, item.thumbnailImage());
            ps.setString(11, item.modifiedTime());
            ps.setString(12, item.lDongRegnCd());
            ps.setString(13, item.lDongSignguCd());
            ps.setString(14, item.category1());
            ps.setString(15, item.category2());
            ps.setString(16, item.category3());
        });
    }
}