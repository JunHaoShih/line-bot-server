package io.github.junhaoshih.linebotserver.repository;

import io.github.junhaoshih.linebotserver.data.cpc.CPCOilPriceHistory;
import io.github.junhaoshih.linebotserver.enums.CPCOilType;

import java.util.List;

/**
 * 中油油價Dao介面
 */
public interface CPCOilRepository {

    /**
     * 取得中油最近五周的油價歷史
     * @param oilType 中國石油的油類
     * @return 中油最近五周的油價歷史
     */
    List<CPCOilPriceHistory> getLatestOilHistories(CPCOilType oilType);
    
    //List<CpcOilPriceGroup> getOilPriceHistories(CPCOilType oilType);
}
