package io.github.junhaoshih.linebotserver.data.cwb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原素值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementValue {
    /**
     * 原素值
     */
    private String value;

    /**
     * 測量單位
     */
    private String measures;
}
