package io.github.junhaoshih.linebotserver.data;

import io.github.junhaoshih.linebotserver.data.FirstPrizeNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 發票中獎資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePrizeResult {
    /**
     * 發票兌獎標題
     */
    private String title;

    /**
     * 特別獎編號
     */
    private String specialPrizeNumber;

    /**
     * 特獎編號
     */
    private String superPrizeNumber;

    /**
     * 頭獎編號清單
     */
    private List<FirstPrizeNumber> firstPrizeNumbers = new ArrayList<>();

    /**
     * 增開六獎
     */
    private List<String> extraSixthPrizeNumbers = new ArrayList<>();
}
