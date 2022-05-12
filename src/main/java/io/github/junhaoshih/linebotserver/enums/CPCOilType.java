package io.github.junhaoshih.linebotserver.enums;

/**
 * 中國石油的油類
 */
public enum CPCOilType {
    /**
     * 未定義
     */
    Undefined(-1, ""),
    /**
     * 92無鉛汽油
     */
    UnleadedGasoline92(1, "92無鉛汽油"),
    /**
     * 95無鉛汽油
     */
    UnleadedGasoline95(2, "95無鉛汽油"),
    /**
     * 98無鉛汽油
     */
    UnleadedGasoline98(3, "98無鉛汽油");

    private int value;
    private String name;

    private CPCOilType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 取得中油油類在中油webservice中對應的int數值
     * @return 中油webservice中對應的int數值
     */
    public int getValue() {
        return value;
    }

    /**
     * 中油油類名稱
     * @return 中油油類名稱
     */
    public String getName() {
        return name;
    }

    public static CPCOilType getByValue(int value) {
        switch (value) {
            case 1:
                return CPCOilType.UnleadedGasoline92;
            case 2:
                return CPCOilType.UnleadedGasoline95;
            case 3:
                return CPCOilType.UnleadedGasoline98;
            default:
                return CPCOilType.Undefined;
        }
    }

    public static CPCOilType getByName(String name) {
        switch (name) {
            case "92":
            case "92無鉛汽油":
            case "無鉛汽油92":
                return CPCOilType.UnleadedGasoline92;
            case "95":
            case "95無鉛汽油":
            case "無鉛汽油95":
                return CPCOilType.UnleadedGasoline95;
            case "98":
            case "98無鉛汽油":
            case "無鉛汽油98":
                return CPCOilType.UnleadedGasoline98;
            default:
                return CPCOilType.Undefined;
        }
    }
}
