package io.github.junhaoshih.linebotserver.enums.cwb;

import java.util.List;

public enum County {
    /**
     * 未定義
     */
    Undefined("未知", ""),
    /**
     * 臺中市
     */
    TaichungCity("臺中市", "台中市", "中市", "台中", "臺中"),
    /**
     * 臺北市
     */
    TaipeiCity("臺北市", "台北市", "臺北", "台北"),
    /**
     * 新北市
     */
    NewTaipeiCity("新北市", "新北"),
    /**
     * 宜蘭縣
     */
    YilanCounty("宜蘭縣"),
    /**
     * 桃園
     */
    TaoyuanCity("桃園市", "桃園"),
    /**
     * 新竹縣
     */
    HsinchuCounty("新竹縣"),
    /**
     * 新竹市
     */
    HsinchuCity("新竹市"),
    /**
     * 苗栗縣
     */
    MiaoliCounty("苗栗縣", "苗栗"),
    /**
     * 彰化縣
     */
    ChanghuaCounty("彰化縣", "彰化"),
    /**
     * 南投縣
     */
    NantouCounty("南投縣", "南投"),
    /**
     * 雲林縣
     */
    YunlinCounty("雲林縣", "雲林"),
    /**
     * 嘉義縣
     */
    ChiayiCounty("嘉義縣"),
    /**
     * 嘉義市
     */
    ChiayiCity("嘉義市"),
    /**
     * 屏東縣
     */
    PingtungCounty("屏東縣", "屏東"),
    /**
     * 臺東縣
     */
    TaitungCounty("臺東縣", "臺東"),
    /**
     * 花蓮縣
     */
    HualienCounty("花蓮縣", "花蓮"),
    /**
     * 澎湖縣
     */
    PenghuCounty("澎湖縣", "澎湖"),
    /**
     * 基隆市
     */
    KeelungCity("基隆市", "基隆"),
    /**
     * 高雄市
     */
    KaohsiungCity("高雄市", "高雄"),
    /**
     * 臺南市
     */
    TainanCity("臺南市", "台南市", "臺南", "台南"),
    /**
     * 連江縣
     */
    LianjiangCounty("連江縣", "連江"),
    /**
     * 金門縣
     */
    KinmenCounty("金門縣", "金門");

    /**
     * 縣市名稱
     */
    private final String countyName;

    /**
     * 縣市別名
     */
    private final List<String> aliases;

    County(String name, String... aliases) {
        this.countyName = name;
        this.aliases = List.of(aliases);
    }

    public String getCountyName() {
        return countyName;
    }

    /**
     * 嘗試用字串取得縣市
     * @param name 縣市名稱
     * @return 縣市，若找不到則回傳null
     */
    public static County tryGetByName(String name) {
        for (County county : values()) {
            if (county.countyName.equals(name) || county.aliases.contains(name)) {
                return county;
            }
        }
        return  Undefined;
    }

    /**
     * 用字串取得縣市
     * @param name 縣市名稱
     * @return 縣市
     * @throws CountyNotFoundException 若找不到則拋出此Exception
     */
    public static County getByName(String name) throws CountyNotFoundException {
        County returnCounty = tryGetByName(name);
        if (returnCounty == null)
            throw new CountyNotFoundException("找不到縣市:「" + name + "」");
        return returnCounty;
    }
}
