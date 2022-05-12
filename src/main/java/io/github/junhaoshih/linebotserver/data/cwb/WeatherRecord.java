package io.github.junhaoshih.linebotserver.data.cwb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 中央氣象局open data api的氣象紀錄
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRecord {
    /**
     * 縣市區域
     */
    private List<CityCounty> locations;
}
