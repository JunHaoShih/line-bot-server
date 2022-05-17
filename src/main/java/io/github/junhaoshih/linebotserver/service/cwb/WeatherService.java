package io.github.junhaoshih.linebotserver.service.cwb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import io.github.junhaoshih.linebotserver.builder.cwb.WeatherApiUrlBuilder;
import io.github.junhaoshih.linebotserver.data.cwb.WeatherApiResult;
import io.github.junhaoshih.linebotserver.data.cwb.WeatherElement;
import io.github.junhaoshih.linebotserver.data.roc.RocCounty;
import io.github.junhaoshih.linebotserver.data.roc.RocRegion;
import io.github.junhaoshih.linebotserver.enums.cwb.County;
import io.github.junhaoshih.linebotserver.enums.cwb.CountyNotFoundException;
import io.github.junhaoshih.linebotserver.enums.cwb.Interval;
import io.github.junhaoshih.linebotserver.enums.cwb.WeatherElementEnum;
import io.github.junhaoshih.linebotserver.repository.RocCountyRepository;
import io.github.junhaoshih.linebotserver.view_model.WeatherBubble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 氣象service，目前主要功能為取得中央氣象局資訊
 */
@Service
public class WeatherService {

    @Autowired
    private WeatherApiUrlBuilder urlBuilder;

    @Autowired
    private RocCountyRepository rocCountyRepository;

    @Value("${weather.cwb.api.two-day-count}")
    private int twoDayCount;

    @Value("${weather.cwb.api.weather-icon.day}")
    private String dayIconUrl;

    @Value("${weather.cwb.api.weather-icon.night}")
    private String nightIconUrl;

    /**
     * 取得天系資訊的LINE Message
     * @param args 使用者input(0是指令，1是縣市，2是地區)
     * @param event 對話事件，目前沒用到
     * @return 天系資訊的LINE Message
     */
    public Message getWeatherMessage(String[] args, MessageEvent<TextMessageContent> event) {
        String fullUrl;
        try {
            if (args.length <= 2)
                throw new IllegalArgumentException("請輸入正確格式「!天氣 縣市 地區」");

            String countyStr = args[1];
            String regionStr = args[2];

            County county = County.getByName(countyStr);

            boolean isRegionExist = rocCountyRepository.isRegionExist(county, regionStr);
            if (!isRegionExist)
                throw new IllegalArgumentException("找不到區域:「" + regionStr + "」");

            fullUrl = urlBuilder
                    .withCounty(county, Interval.TwoDays)
                    .withLocationName(regionStr)
                    .withWeatherElement(WeatherElementEnum.PoP12h)
                    .withWeatherElement(WeatherElementEnum.Wx)
                    .withWeatherElement(WeatherElementEnum.T)
                    .withSort()
                    .build();

            WeatherApiResult war = getWeatherApiResult(fullUrl);

            return toMessage(war, county, regionStr);
        } catch (IOException | InterruptedException | CountyNotFoundException e) {
            e.printStackTrace();
            return new TextMessage("發生錯誤: " + e.getMessage());
        }
    }

    private WeatherApiResult getWeatherApiResult(String url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(10000))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(10000))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), WeatherApiResult.class);
    }

    private Message toMessage(WeatherApiResult war, County county, String regionStr) {
        return FlexMessage.builder()
                .altText("Hello")
                .contents(
                        Carousel.builder()
                                .contents(getWeatherBubbles(war, county, regionStr))
                                .build()
                )
                .build();
    }

    private List<Bubble> getWeatherBubbles(WeatherApiResult war, County county, String regionStr) {
        List<Bubble> bubbles = new ArrayList<>();
        List<WeatherElement> weatherElements = war.getRecords().getLocations().get(0).getLocation().get(0).getWeatherElement();
        List<WeatherBubble> weatherBubbles = WeatherBubble.createBubbles(weatherElements, twoDayCount);
        for (int i = 0; i < weatherBubbles.size(); i++) {
            bubbles.add(getWeatherBubble(weatherBubbles.get(i), i, county, regionStr));
        }
        return bubbles;
    }

    private Bubble getWeatherBubble(WeatherBubble weatherBubble, int index, County county, String regionStr) {

        return Bubble.builder()
                .size(Bubble.BubbleSize.MICRO)
                .header(getHeaderBox(weatherBubble, index, county, regionStr))
                .body(getBodyBox(weatherBubble))
                .build();
    }

    private Box getHeaderBox(WeatherBubble weatherBubble, int index, County county, String regionStr) {
        String colorStr = index % 2 == 0 ? "#27ACB2" : "#FF6B6E";
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .backgroundColor(colorStr)
                .paddingAll("12px")
                .paddingTop("19px")
                .paddingBottom("16px")
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text(county.getCountyName() + " " + regionStr)
                                .size("lg")
                                .color("#ffffff")
                                .build());
                        add(Text.builder()
                                .text(weatherBubble.getDateString())
                                .size("md")
                                .color("#ffffff")
                                .build());
                        add(Text.builder()
                                .text(weatherBubble.getTimeString())
                                .margin("lg")
                                .size("xs")
                                .color("#ffffff")
                                .build());
                    }
                })
                .build();
    }

    private Box getBodyBox(WeatherBubble weatherBubble) {

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing("md")
                .paddingAll("12px")
                .contents(new ArrayList<>() {
                    {
                        add(getWxBox(weatherBubble));
                        add(Separator.builder().build());
                        add(getPoP12hBox(weatherBubble));
                        add(Separator.builder().build());
                        add(getTBox(weatherBubble));
                    }
                })
                .build();
    }

    private Box getWxBox(WeatherBubble weatherBubble) {
        String iconUrl;
        if (isNight(weatherBubble.getTimeString()))
            iconUrl = String.format(nightIconUrl, weatherBubble.getWxValue());
        else
            iconUrl = String.format(dayIconUrl, weatherBubble.getWxValue());

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(new ArrayList<>() {
                    {
                        add(Image.builder()
                                .url(URI.create(iconUrl))
                                .size("xs")
                                .build());
                        add(Text.builder()
                                .text(weatherBubble.getWxString())
                                .align(FlexAlign.CENTER)
                                .build());
                    }
                })
                .build();
    }

    /**
     * 判斷時段是否為晚上，根據中央氣象局，晚上時段為1800~0600
     * @param timeString 時間字串
     * @return 是否為晚上
     */
    private boolean isNight(String timeString) {
        int timeInt = Integer.parseInt(timeString);
        if ((timeInt >= 1800 && timeInt <= 2400) || (timeInt >= 0000 && timeInt < 0600))
            return true;
        return false;
    }

    /**
     * 取得12小時降雨機率的Box
     * @param weatherBubble
     * @return
     */
    private Box getPoP12hBox(WeatherBubble weatherBubble) {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(new ArrayList<>() {
                    {
                        add(getPoPBox(weatherBubble.getPoP12h()));
                        add(getPoPProgressBar(weatherBubble.getPoP12h()));
                    }
                })
                .build();
    }

    /**
     * 降雨機率Box
     * @param percentage
     * @return
     */
    private Box getPoPBox(String percentage) {
        return Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("降雨機率")
                                .align(FlexAlign.START)
                                .build());
                        add(Separator.builder().build());
                        add(Text.builder()
                                .text(percentage)
                                .align(FlexAlign.CENTER)
                                .build());
                    }
                })
                .build();
    }

    private Box getPoPProgressBar(String percentage) {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .margin("sm")
                .height("6px")
                .backgroundColor("#9FD8E36E")
                .contents(new ArrayList<>() {
                    {
                        add(Box.builder()
                                .layout(FlexLayout.VERTICAL)
                                .width(percentage + "%")
                                .height("6px")
                                .backgroundColor("#0D8186")
                                .contents(new ArrayList<>() {
                                    {
                                        add(Filler.builder().build());
                                    }
                                })
                                .build());
                    }
                })
                .build();
    }

    private Box getTBox(WeatherBubble weatherBubble) {
        return Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(new ArrayList<>() {
                    {
                        add(Text.builder()
                                .text("溫度")
                                .build());
                        add(Separator.builder().build());
                        add(Text.builder()
                                .text(weatherBubble.getT())
                                .align(FlexAlign.CENTER)
                                .build());
                    }
                })
                .build();
    }
}
