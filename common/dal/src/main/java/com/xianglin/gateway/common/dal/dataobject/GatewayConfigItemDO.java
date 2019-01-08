/**
 * 
 */
package com.xianglin.gateway.common.dal.dataobject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 网关配置项数据对象
 * 
 * @author pengpeng 2016年1月20日下午2:40:57
 */
public class GatewayConfigItemDO extends BaseDO {

	/** 配置项key */
	private String itemKey;

	/** 配置项value */
	private String itemValue;

	/**
	 * @return the itemKey
	 */
	public String getItemKey() {
		return itemKey;
	}

	/**
	 * @param itemKey
	 *            the itemKey to set
	 */
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue
	 *            the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
