package io.github.junhaoshih.linebotserver.data.cwb;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 鄉鎮市區
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
    /**
     * 鄉鎮市區名稱
     */
    private String locationName;

    /**
     * 地址編碼
     */
    @JsonSetter("geocode")
    private String geoCode;

    /**
     * 緯度
     */
    private String lat;

    /**
     * 經度
     */
    private String lon;

    private List<WeatherElement> weatherElement;
}
