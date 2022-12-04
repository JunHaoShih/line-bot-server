package io.github.junhaoshih.linebotserver.data.cpc;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中油油價
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CpcOilPrice {
	
	/**
	 * 油類名稱
	 */
	private String name;
	
	/**
	 * 價格
	 */
	private BigDecimal y;

	/**
	 * Group id
	 */
	@JsonProperty("GroupID")
	private int groupId;
}
