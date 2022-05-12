package io.github.junhaoshih.linebotserver.view_model;

import io.github.junhaoshih.linebotserver.data.cwb.Time;
import io.github.junhaoshih.linebotserver.data.cwb.WeatherElement;
import io.github.junhaoshih.linebotserver.enums.cwb.WeatherElementEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 儲存中央氣象局資料的view model，用於承接API中的WeatherElement，轉換成WeatherBubble這個比較好使用的資料結構
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherBubble {
    /**
     * 起始時間
     */
    private Date date;
    /**
     * 天氣現象
     */
    private String wxString;
    /**
     * 天氣現象Icon代表值
     */
    private String wxValue;
    /**
     * 12小時降雨機率
     */
    private String poP12h;
    /**
     * 溫度
     */
    private String t;

    /**
     * 取得日期(MM/dd)
     * @return 日期字串
     */
    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        return sdf.format(date);
    }

    /**
     * 取得時間(HHmm)
     * @return 時間字串
     */
    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return sdf.format(date);
    }

    /**
     * 將WeatherElement清單轉換成WeatherBubble清單
     * @param weatherElements WeatherElement清單
     * @param count 顯示數量，不要超過6
     * @return WeatherBubble清單
     */
    public static List<WeatherBubble> createBubbles(List<WeatherElement> weatherElements, int count) {
        List<WeatherBubble> bubbles = new ArrayList<>();
        Stream<WeatherElement> pop12hStream = weatherElements.stream()
                .filter(we -> we.getElementName().equals(WeatherElementEnum.PoP12h.getElementName()));
        Stream<WeatherElement> wxStream = weatherElements.stream()
                .filter(we -> we.getElementName().equals(WeatherElementEnum.Wx.getElementName()));
        Stream<WeatherElement> tStream = weatherElements.stream()
                .filter(we -> we.getElementName().equals(WeatherElementEnum.T.getElementName()));

        WeatherElement pop12hElement = pop12hStream.collect(Collectors.toList()).get(0);
        WeatherElement wxElement = wxStream.collect(Collectors.toList()).get(0);
        WeatherElement tElement = tStream.collect(Collectors.toList()).get(0);

        List<Date> startTimes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Time time = pop12hElement.getTime().get(i);
            startTimes.add(time.getStartTime());
            WeatherBubble bubble = new WeatherBubble();
            bubble.setDate(time.getStartTime());
            bubble.setPoP12h(time.getElementValue().get(0).getValue());
            bubbles.add(bubble);
        }

        initializeWxElement(startTimes, wxElement, bubbles);

        initializeTElement(startTimes, tElement, bubbles);

        return bubbles;
    }

    /**
     * 初始化天氣現象
     * @param startTimes 要顯示的時間清單
     * @param wxElement 天氣現象Element
     * @param bubbles 要初始化的WeatherBubble清單
     */
    private static void initializeWxElement(List<Date> startTimes, WeatherElement wxElement, List<WeatherBubble> bubbles) {
        int i = 0;
        for (Time time : wxElement.getTime()) {
            if (!startTimes.contains(time.getStartTime()))
                continue;
            bubbles.get(i).setWxString(time.getElementValue().get(0).getValue());
            bubbles.get(i).setWxValue(time.getElementValue().get(1).getValue());
            i++;
        }
    }

    /**
     * 初始化溫度
     * @param startTimes 要顯示的時間清單
     * @param tElement 溫度Element
     * @param bubbles 要初始化的WeatherBubble清單
     */
    private static void initializeTElement(List<Date> startTimes, WeatherElement tElement, List<WeatherBubble> bubbles) {
        int i = 0;
        for (Time time : tElement.getTime()) {
            if (!startTimes.contains(time.getDataTime()))
                continue;
            bubbles.get(i).setT(time.getElementValue().get(0).getValue());
            i++;
        }
    }
}