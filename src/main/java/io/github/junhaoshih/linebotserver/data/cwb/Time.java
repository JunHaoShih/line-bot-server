package io.github.junhaoshih.linebotserver.data.cwb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 氣象元素的時間
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    /**
     * 起始時間
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 結束時間
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 資料時間
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    /**
     * 數值清單
     */
    private List<ElementValue> elementValue;
}
