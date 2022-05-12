package io.github.junhaoshih.linebotserver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 發票頭獎編號，因為需要取得編號後三碼，因此特別做這個class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstPrizeNumber {

    /**
     * 發票編號
     */
    private String prizeNumber;

    /**
     * 頭獎編號前幾碼
     * @return 頭獎編號前幾碼
     */
    public String getFirstPrizePrefix() {
        return prizeNumber.substring(0, prizeNumber.length() - 3);
    }

    /**
     * 頭獎編號後三碼
     * @return 頭獎編號後三碼
     */
    public String getFirstPrizeSuffix() {
        return prizeNumber.substring(prizeNumber.length() - 3);
    }
}
