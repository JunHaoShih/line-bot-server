package io.github.junhaoshih.linebotserver.repository;

import io.github.junhaoshih.linebotserver.data.roc.RocCounty;
import io.github.junhaoshih.linebotserver.enums.cwb.County;

import java.io.IOException;
import java.util.List;

public interface RocCountyRepository {
    /**
     * 取得縣市與地區資訊(不含道路)
     * @return 縣市資訊清單
     * @throws IOException
     * @throws InterruptedException
     */
    List<RocCounty> getRocCounties() throws IOException, InterruptedException;

    /**
     * 檢查地區是否存在
     * @param county 地區所在縣市
     * @param regionStr 地區字串
     * @return 是否存在
     * @throws IOException
     * @throws InterruptedException
     */
    boolean isRegionExist(County county, String regionStr) throws IOException, InterruptedException;
}
