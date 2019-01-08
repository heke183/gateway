/**
 * 
 */
package com.xianglin.gateway.core.service.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.xianglin.gateway.common.dal.daointerface.GatewayConfigItemDAO;
import com.xianglin.gateway.common.dal.dataobject.GatewayConfigItemDO;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;

/**
 * 网关配置项仓储实现类
 * 
 * @author pengpeng 2016年1月20日下午5:29:08
 */
public class GatewayConfigRepositoryImpl implements GatewayConfigRepository, InitializingBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GatewayConfigRepositoryImpl.class);

	/** cache */
	private Map<String, String> cache = new HashMap<String, String>();

	/** gatewayConfigItemDAO */
	private GatewayConfigItemDAO gatewayConfigItemDAO;

	/** systemName */
	private String systemName;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		refresh();
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayConfigRepository#refresh()
	 */
	@Override
	public void refresh() {
		logger.info("gatewayName:{}", systemName);
		List<GatewayConfigItemDO> list = gatewayConfigItemDAO.getAllEnable(systemName);
		Map<String, String> map = new HashMap<String, String>();
		for (GatewayConfigItemDO item : list) {
			if (StringUtils.isEmpty(item.getItemKey())) {
				logger.error("empty gateway config item key!");
				return;
			}
			map.put(item.getItemKey(), item.getItemValue());
			logger.info("load GatewayConfigItem:{}", item);
		}
		cache = map;
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayConfigRepository#get(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String get(String key, String defaultValue) {
		String value = cache.get(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayConfigRepository#get(java.lang.String,
	 *      java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T get(String key, T defaultValue, Class<T> clazz) {
		String json = cache.get(key);
		if (StringUtils.isEmpty(json)) {
			return defaultValue;
		}
		return JSON.parseObject(json, clazz);
	}

	/**
	 * @param gatewayConfigItemDAO
	 *            the gatewayConfigItemDAO to set
	 */
	public void setGatewayConfigItemDAO(GatewayConfigItemDAO gatewayConfigItemDAO) {
		this.gatewayConfigItemDAO = gatewayConfigItemDAO;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
