package io.github.junhaoshih.linebotserver.data.roc;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RocCounty {
    @JsonSetter("CityName")
    private String cityName;

    @JsonSetter("CityEngName")
    private String cityEngName;

    @JsonSetter("AreaList")
    private List<RocRegion> areaList;
}
