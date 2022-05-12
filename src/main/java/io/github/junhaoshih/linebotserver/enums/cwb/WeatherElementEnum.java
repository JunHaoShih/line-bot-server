package io.github.junhaoshih.linebotserver.enums.cwb;

public enum WeatherElementEnum {
    Undefined("未知"),
    /**
     * 12小時降雨機率
     */
    PoP12h("PoP12h"),
    /**
     * 天氣現象
     */
    Wx("Wx"),
    /**
     * 溫度
     */
    T("T");

    /**
     * element名稱
     */
    private final String elementName;

    WeatherElementEnum(String elementName) {
        this.elementName = elementName;
    }

    /**
     * 用element名稱取得列舉
     * @param name element名稱
     * @return 對應列舉，若找不到則回傳Undefined
     */
    public static WeatherElementEnum getByName(String name) {
        switch (name) {
            case "PoP12h":
                return PoP12h;
            case "Wx":
                return Wx;
            case "T":
                return T;
            default:
                return Undefined;
        }
    }

    /**
     * 取得列舉的element名稱
     * @return element名稱
     */
    public String getElementName() {
        return elementName;
    }
}
