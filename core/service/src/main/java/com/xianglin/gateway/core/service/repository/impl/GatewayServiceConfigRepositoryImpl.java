/**
 * 
 */
package com.xianglin.gateway.core.service.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.xianglin.gateway.common.dal.daointerface.GatewayServiceConfigDAO;
import com.xianglin.gateway.common.dal.dataobject.GatewayServiceConfigDO;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.enums.Protocol;
import com.xianglin.gateway.common.service.spi.model.enums.RpcType;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;

/**
 * 网关服务配置信息仓储实现类
 * 
 * @author pengpeng 2015年4月28日下午3:09:12
 */
public class GatewayServiceConfigRepositoryImpl implements GatewayServiceConfigRepository {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceConfigRepositoryImpl.class);

	/** cache */
	private Map<String, GatewayServiceConfig> cache = new HashMap<String, GatewayServiceConfig>();

	/** temp cache */
	private Map<String, GatewayServiceConfig> tempCache = new HashMap<String, GatewayServiceConfig>();

	/** gatewayServiceConfigDAO */
	private GatewayServiceConfigDAO gatewayServiceConfigDAO;

	/** systemName */
	private String systemName;

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository#reload()
	 */
	@Override
	public void refresh() {
		List<GatewayServiceConfigDO> list = gatewayServiceConfigDAO.getAllEnable(systemName);
		if (CollectionUtils.isEmpty(list)) {
			logger.info("load GatewayServiceConfig from db, count:0");
		}
		synchronized (tempCache) {
			tempCache.clear();
			for (GatewayServiceConfigDO item : list) {
				GatewayServiceConfig gatewayServiceConfig = createGatewayServiceConfig(item);
				tempCache.put(gatewayServiceConfig.getAlias(), gatewayServiceConfig);
				logger.info("load GatewayServiceConfig:{}", gatewayServiceConfig);
			}
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository#rollback()
	 */
	@Override
	public void rollback() {
		synchronized (tempCache) {
			tempCache.clear();
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository#complete()
	 */
	@Override
	public void complete() {
		synchronized (tempCache) {
			cache = tempCache;
			tempCache = new HashMap<String, GatewayServiceConfig>();
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository#get(java.lang.String)
	 */
	@Override
	public GatewayServiceConfig get(String operation) {
		return cache.get(operation);
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository#getAll()
	 */
	@Override
	public List<GatewayServiceConfig> getAll() {
		return new ArrayList<GatewayServiceConfig>(tempCache.values());
	}

	/**
	 * 根据配置创建GatewayServiceConfig对象
	 * 
	 * @param gatewayServiceConfigDO
	 * @return
	 */
	private GatewayServiceConfig createGatewayServiceConfig(GatewayServiceConfigDO gatewayServiceConfigDO) {
		GatewayServiceConfig gatewayServiceConfig = new GatewayServiceConfig();
		gatewayServiceConfig.setAlias(gatewayServiceConfigDO.getAlias());
		gatewayServiceConfig.setSystemName(gatewayServiceConfigDO.getSystemName());
		gatewayServiceConfig.setServiceId(gatewayServiceConfigDO.getServiceId());
		gatewayServiceConfig.setRpcType(RpcType.valueOf(gatewayServiceConfigDO.getRpcType()));
		gatewayServiceConfig.setSpiVersion(gatewayServiceConfigDO.getSpiVersion());
		gatewayServiceConfig.setGatewayServiceInterface(
				getGatewayServiceInterface(gatewayServiceConfigDO.getGatewayServiceInterfaceName()));
		gatewayServiceConfig.setProtocol(Protocol.valueOf(gatewayServiceConfigDO.getProtocol()));
		gatewayServiceConfig.setTimeout(gatewayServiceConfigDO.getTimeout());
		gatewayServiceConfig.setChangeSessionBeforeInvoke(gatewayServiceConfigDO.isChangeSession());
		gatewayServiceConfig.setDeleteSessionAfterInvoke(gatewayServiceConfigDO.isDeleteSession());
		gatewayServiceConfig.setNeedLoginBeforeInvoke(gatewayServiceConfigDO.isNeedLogin());
		gatewayServiceConfig.setUseETag(gatewayServiceConfigDO.isUseETag());
		return gatewayServiceConfig;
	}

	/**
	 * 根据网关服务接口名称取得网关服务接口的Class对象
	 * 
	 * @param gatewayServiceInterfaceName
	 * @return
	 */
	private Class<?> getGatewayServiceInterface(String gatewayServiceInterfaceName) {
		Class<?> gatewayServiceInterface = null;
		try {
			gatewayServiceInterface = Class.forName(gatewayServiceInterfaceName);
		} catch (Throwable e) {
			logger.error("can not find class:" + gatewayServiceInterfaceName, e);
			throw new RuntimeException(e);
		}
		return gatewayServiceInterface;
	}

	/**
	 * @param gatewayServiceConfigDAO
	 *            the gatewayServiceConfigDAO to set
	 */
	public void setGatewayServiceConfigDAO(GatewayServiceConfigDAO gatewayServiceConfigDAO) {
		this.gatewayServiceConfigDAO = gatewayServiceConfigDAO;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
