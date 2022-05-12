package io.github.junhaoshih.linebotserver.repository;

import io.github.junhaoshih.linebotserver.data.InvoicePrizeResult;

/**
 * 發票Dao介面
 */
public interface InvoiceRepository {

    /**
     * 取得最新發票中獎資訊
     * @return 發票中獎資訊
     */
    InvoicePrizeResult getLatestInvoicePrizeResult();
}
