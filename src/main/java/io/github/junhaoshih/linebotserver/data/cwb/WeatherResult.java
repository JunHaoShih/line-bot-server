package io.github.junhaoshih.linebotserver.data.cwb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResult {
    private String resource_id;
    private List<DataField> fields;
}
