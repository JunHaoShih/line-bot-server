package io.github.junhaoshih.linebotserver.data.cpc;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中油油價歷史
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpcOilPriceGroup {

	/**
	 * 價格群組名稱(目前是日期)
	 */
	private String name;
	
	/**
	 * 油價清單
	 */
	private List<CpcOilPrice> data;
}
