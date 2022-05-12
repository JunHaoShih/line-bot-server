package io.github.junhaoshih.linebotserver.data.cwb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中央氣象局open data api的結果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherApiResult {
    /**
     * 是否取得成功
     */
    private String success;

    /**
     * 回傳資料結果的型態
     */
    private WeatherResult result;

    /**
     * 回傳資料結果
     */
    private WeatherRecord records;
}
