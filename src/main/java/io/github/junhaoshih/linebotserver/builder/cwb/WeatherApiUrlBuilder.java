package io.github.junhaoshih.linebotserver.builder.cwb;

import io.github.junhaoshih.linebotserver.enums.cwb.County;
import io.github.junhaoshih.linebotserver.enums.cwb.Interval;
import io.github.junhaoshih.linebotserver.enums.cwb.WeatherElementEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * 建構中央氣象局API網址的builder
 */
@Repository
public class WeatherApiUrlBuilder {
    @Value("${weather.cwb.api.url-prefix}")
    private String baseUrl;

    @Value("${weather.cwb.api.auth-key}")
    private String authKey;

    @Value("${weather.cwb.api.yilan-county.two-day}")
    private String yilanCounty2Day;

    @Value("${weather.cwb.api.yilan-county.one-week}")
    private String yilanCounty1Week;

    @Value("${weather.cwb.api.taoyuan-city.two-day}")
    private String taoyuanCity2Day;

    @Value("${weather.cwb.api.taoyuan-city.one-week}")
    private String taoyuanCity1Week;

    @Value("${weather.cwb.api.hsinchu-county.two-day}")
    private String hsinchuCounty2Day;

    @Value("${weather.cwb.api.hsinchu-county.one-week}")
    private String hsinchuCounty1Week;

    @Value("${weather.cwb.api.miaoli-county.two-day}")
    private String miaoliCounty2Day;

    @Value("${weather.cwb.api.miaoli-county.one-week}")
    private String miaoliCounty1Week;

    @Value("${weather.cwb.api.changhua-county.two-day}")
    private String changhuaCounty2Day;

    @Value("${weather.cwb.api.changhua-county.one-week}")
    private String changhuaCounty1Week;

    @Value("${weather.cwb.api.nantou-county.two-day}")
    private String nantouCounty2Day;

    @Value("${weather.cwb.api.nantou-county.one-week}")
    private String nantouCounty1Week;

    @Value("${weather.cwb.api.yunlin-county.two-day}")
    private String yunlinCounty2Day;

    @Value("${weather.cwb.api.yunlin-county.one-week}")
    private String yunlinCounty1Week;

    @Value("${weather.cwb.api.chiayi-county.two-day}")
    private String chiayiCounty2Day;

    @Value("${weather.cwb.api.chiayi-county.one-week}")
    private String chiayiCounty1Week;

    @Value("${weather.cwb.api.pingtung-county.two-day}")
    private String pingtungCounty2Day;

    @Value("${weather.cwb.api.pingtung-county.one-week}")
    private String pingtungCounty1Week;

    @Value("${weather.cwb.api.taitung-county.two-day}")
    private String taitungCounty2Day;

    @Value("${weather.cwb.api.taitung-county.one-week}")
    private String taitungCounty1Week;

    @Value("${weather.cwb.api.hualien-county.two-day}")
    private String hualienCounty2Day;

    @Value("${weather.cwb.api.hualien-county.one-week}")
    private String hualienCounty1Week;

    @Value("${weather.cwb.api.penghu-county.two-day}")
    private String penghuCounty2Day;

    @Value("${weather.cwb.api.penghu-county.one-week}")
    private String penghuCounty1Week;

    @Value("${weather.cwb.api.keelung-city.two-day}")
    private String keelungCity2Day;

    @Value("${weather.cwb.api.keelung-city.one-week}")
    private String keelungCity1Week;

    @Value("${weather.cwb.api.hsinchu-city.two-day}")
    private String hsinchuCity2Day;

    @Value("${weather.cwb.api.hsinchu-city.one-week}")
    private String hsinchuCity1Week;

    @Value("${weather.cwb.api.chiayi-city.two-day}")
    private String chiayiCity2Day;

    @Value("${weather.cwb.api.chiayi-city.one-week}")
    private String chiayiCity1Week;

    @Value("${weather.cwb.api.taipei.two-day}")
    private String taipei2Day;

    @Value("${weather.cwb.api.taipei.one-week}")
    private String taipei1Week;

    @Value("${weather.cwb.api.kaohsiung-city.two-day}")
    private String kaohsiungCity2Day;

    @Value("${weather.cwb.api.kaohsiung-city.one-week}")
    private String kaohsiungCity1Week;

    @Value("${weather.cwb.api.new-taipei.two-day}")
    private String newTaipei2Day;

    @Value("${weather.cwb.api.new-taipei.one-week}")
    private String newTaipei1Week;

    @Value("${weather.cwb.api.taichung.two-day}")
    private String taichung2Day;

    @Value("${weather.cwb.api.taichung.one-week}")
    private String taichung1week;

    @Value("${weather.cwb.api.tainan-city.two-day}")
    private String tainanCity2Day;

    @Value("${weather.cwb.api.tainan-city.one-week}")
    private String tainanCity1Week;

    @Value("${weather.cwb.api.lianjiang-county.two-day}")
    private String lianjiangCounty2Day;

    @Value("${weather.cwb.api.lianjiang-county.one-week}")
    private String lianjiangCounty1Week;

    @Value("${weather.cwb.api.kinmen-county.two-day}")
    private String kinmenCounty2Day;

    @Value("${weather.cwb.api.kinmen-county.one-week}")
    private String kinmenCounty1Week;

    private County county;
    private Interval interval;
    private String locationName;
    private ArrayList<WeatherElementEnum> weatherElementEnums = new ArrayList<>();
    private String sortString = "";

    /**
     * 設定縣市
     * @param county 縣市列舉
     * @param interval 時間區間列舉
     * @return builder
     */
    public WeatherApiUrlBuilder withCounty(County county, Interval interval) {
        this.county = county;
        this.interval = interval;
        return this;
    }

    /**
     * 設定區域
     * @param locationName 區域名稱(詳細請參考中央氣象局API說明)
     * @return builder
     */
    public WeatherApiUrlBuilder withLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    /**
     * 設定要取得的天氣元素(如降雨綠、氣溫等)
     * @param weatherElementEnum 天氣元素列舉
     * @return builder
     */
    public WeatherApiUrlBuilder withWeatherElement(WeatherElementEnum weatherElementEnum) {
        if (!weatherElementEnums.contains(weatherElementEnum))
            weatherElementEnums.add(weatherElementEnum);
        return this;
    }

    /**
     * 依照時間排序
     * @return builder
     */
    public WeatherApiUrlBuilder withSort() {
        sortString = "sort=time";
        return this;
    }

    /**
     * 建立API的url
     * @return API的url
     * @throws UnsupportedEncodingException 這個exception基本上不可能發生
     */
    public String build() throws UnsupportedEncodingException {
        StringBuilder finalUrl = new StringBuilder();
        finalUrl.append(baseUrl);
        finalUrl.append(getApiType()).append("?");
        finalUrl.append("Authorization=").append(authKey);
        finalUrl.append("&locationName=").append(URLEncoder.encode(locationName, StandardCharsets.UTF_8));

        if (weatherElementEnums.size() > 0)
            finalUrl.append("&elementName=");
        for (int i = 0; i < weatherElementEnums.size(); i++) {
            WeatherElementEnum weatherEnum = weatherElementEnums.get(i);
            if (i > 0)
                finalUrl.append(",");
            finalUrl.append(weatherEnum.name());
        }

        if (!sortString.isEmpty() && !sortString.isBlank())
            finalUrl.append("&sort=").append(sortString);

        return finalUrl.toString();
    }

    private String getApiType() throws InvalidParameterException {
        if (county == County.YilanCounty) {
            return interval == Interval.TwoDays ? yilanCounty2Day : yilanCounty1Week;
        }
        else if (county == County.TaoyuanCity) {
            return interval == Interval.TwoDays ? taoyuanCity2Day : taoyuanCity1Week;
        }
        else if (county == County.HsinchuCounty) {
            return interval == Interval.TwoDays ? hsinchuCounty2Day : hsinchuCounty1Week;
        }
        else if (county == County.MiaoliCounty) {
            return interval == Interval.TwoDays ? miaoliCounty2Day : miaoliCounty1Week;
        }
        else if (county == County.ChanghuaCounty) {
            return interval == Interval.TwoDays ? changhuaCounty2Day : changhuaCounty1Week;
        }
        else if (county == County.NantouCounty) {
            return interval == Interval.TwoDays ? nantouCounty2Day : nantouCounty1Week;
        }
        else if (county == County.YunlinCounty) {
            return interval == Interval.TwoDays ? yunlinCounty2Day : yunlinCounty1Week;
        }
        else if (county == County.ChiayiCounty) {
            return interval == Interval.TwoDays ? chiayiCounty2Day : chiayiCounty1Week;
        }
        else if (county == County.PingtungCounty) {
            return interval == Interval.TwoDays ? pingtungCounty2Day : pingtungCounty1Week;
        }
        else if (county == County.TaitungCounty) {
            return interval == Interval.TwoDays ? taitungCounty2Day : taitungCounty1Week;
        }
        else if (county == County.HualienCounty) {
            return interval == Interval.TwoDays ? hualienCounty2Day : hualienCounty1Week;
        }
        else if (county == County.PenghuCounty) {
            return interval == Interval.TwoDays ? penghuCounty2Day : penghuCounty1Week;
        }
        else if (county == County.KeelungCity) {
            return interval == Interval.TwoDays ? keelungCity2Day : keelungCity1Week;
        }
        else if (county == County.HsinchuCity) {
            return interval == Interval.TwoDays ? hsinchuCity2Day : hsinchuCity1Week;
        }
        else if (county == County.ChiayiCity) {
            return interval == Interval.TwoDays ? chiayiCity2Day : chiayiCity1Week;
        }
        else if (county == County.TaipeiCity) {
            return interval == Interval.TwoDays ? taipei2Day : taipei1Week;
        }
        else if (county == County.KaohsiungCity) {
            return interval == Interval.TwoDays ? kaohsiungCity2Day : kaohsiungCity1Week;
        }
        else if (county == County.NewTaipeiCity) {
            return interval == Interval.TwoDays ? newTaipei2Day : newTaipei1Week;
        }
        else if (county == County.TaichungCity) {
            return interval == Interval.TwoDays ? taichung2Day : taichung1week;
        }
        else if (county == County.TainanCity) {
            return interval == Interval.TwoDays ? tainanCity2Day : tainanCity1Week;
        }
        else if (county == County.LianjiangCounty) {
            return interval == Interval.TwoDays ? lianjiangCounty2Day : lianjiangCounty1Week;
        }
        else if (county == County.KinmenCounty) {
            return interval == Interval.TwoDays ? kinmenCounty2Day : kinmenCounty1Week;
        }
        throw new InvalidParameterException(county + " and " + interval + " is not valid!");
    }
}
