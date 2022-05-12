package io.github.junhaoshih.linebotserver.data.cpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * 中油油價歷史
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPCOilPriceHistory {

    /**
     * 生效日期
     */
    private ZonedDateTime effectiveDate;

    /**
     * 產品名(油的種類)
     */
    private String productName;

    /**
     * 參考牌價
     */
    private BigDecimal referencePrice;

    /**
     * 計價單位
     */
    private String ChargeUnit;
}
