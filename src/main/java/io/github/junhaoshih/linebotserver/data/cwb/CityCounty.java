package io.github.junhaoshih.linebotserver.data.cwb;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 縣市區域
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCounty {
    /**
     * 縣市區域名稱
     */
    private String locationsName;

    /**
     * dataset描述
     */
    private String datasetDescription;

    /**
     * 資料ID
     */
    @JsonSetter("dataid")
    private String dataId;

    /**
     * 鄉鎮市區
     */
    private List<District> location;
}
