package io.github.junhaoshih.linebotserver.data.cwb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 氣象元素
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherElement {
    /**
     * 元素名稱
     */
    private String elementName;

    /**
     * 描述
     */
    private String description;

    private List<Time> time;
}
