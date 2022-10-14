package io.github.junhaoshih.linebotserver.data.cpc;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中油油價
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpcOilPrice {
	
	/**
	 * 油類名稱
	 */
	private String name;
	
	/**
	 * 價格
	 */
	private BigDecimal y;
}
