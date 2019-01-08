/**
 * 
 */
package com.xianglin.gateway.core.service.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.xianglin.gateway.common.service.spi.GatewayService;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.enums.RpcType;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;
import com.xianglin.gateway.core.service.repository.ReferenceConfigRepository;

/**
 * Dubbo ReferenceConfig仓储实现类
 * 
 * @author pengpeng 2016年1月20日下午7:06:15
 */
public class ReferenceConfigRepositoryImpl implements ReferenceConfigRepository {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ReferenceConfigRepositoryImpl.class);

	/** cache */
	private Map<String, ReferenceConfig<GatewayService<?, ?>>> cache = new HashMap<String, ReferenceConfig<GatewayService<?, ?>>>();

	/** tempCache */
	private Map<String, ReferenceConfig<GatewayService<?, ?>>> tempCache = new HashMap<String, ReferenceConfig<GatewayService<?, ?>>>();

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/** 注册中心配置 */
	private RegistryConfig registryConfig;

	/** systemName */
	private String systemName;
	/**
	 * filter
	 */
	private String filter;

	/**
	 * @see com.xianglin.gateway.core.service.repository.ReferenceConfigRepository#reload()
	 */
	@Override
	public void refresh() {
		List<GatewayServiceConfig> list = gatewayServiceConfigRepository.getAll();
		synchronized (tempCache) {
			tempCache.clear();
			for (GatewayServiceConfig gatewayServiceConfig : list) {
				if (tempCache.containsKey(gatewayServiceConfig.getReferenceConfigId())) {
					continue;
				}
				ReferenceConfig<GatewayService<?, ?>> referenceConfig = createReferenceConfig(gatewayServiceConfig);
				tempCache.put(gatewayServiceConfig.getReferenceConfigId(), referenceConfig);
				logger.info("load dubbo ReferenceConfig:{}", referenceConfig);
			}
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.ReferenceConfigRepository#rollback()
	 */
	@Override
	public void rollback() {
		synchronized (tempCache) {
			destroyReferenceConfig(tempCache);
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.ReferenceConfigRepository#complete()
	 */
	@Override
	public void complete() {
		synchronized (tempCache) {
			Map<String, ReferenceConfig<GatewayService<?, ?>>> oldCache = cache;
			cache = tempCache;
			tempCache = new HashMap<String, ReferenceConfig<GatewayService<?, ?>>>();
			destroyReferenceConfig(oldCache);
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.ReferenceConfigRepository#get(java.lang.String)
	 */
	@Override
	public ReferenceConfig<GatewayService<?, ?>> get(String key) {
		return cache.get(key);
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.ReferenceConfigRepository#getAll()
	 */
	@Override
	public Map<String, ReferenceConfig<GatewayService<?, ?>>> getAll() {
		return Collections.unmodifiableMap(tempCache);
	}

	/**
	 * 创建ReferenceConfig
	 * 
	 * @param gatewayServiceConfig
	 * @return
	 */
	private ReferenceConfig<GatewayService<?, ?>> createReferenceConfig(GatewayServiceConfig gatewayServiceConfig) {
		ReferenceConfig<GatewayService<?, ?>> referenceConfig = new ReferenceConfig<GatewayService<?, ?>>();

		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(systemName);
		referenceConfig.setApplication(applicationConfig);
		referenceConfig.setRegistry(registryConfig);
		if(filter !=null) {
			referenceConfig.setFilter(filter);
		}
		referenceConfig.setInterface(gatewayServiceConfig.getGatewayServiceInterface());
		referenceConfig.setGroup(getGroup(gatewayServiceConfig));
		referenceConfig.setVersion(gatewayServiceConfig.getSpiVersion());
		referenceConfig.setTimeout(gatewayServiceConfig.getTimeout());
		referenceConfig.setProtocol(gatewayServiceConfig.getProtocol().name());
		referenceConfig.setCheck(false);
		referenceConfig.setRetries(0);

		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setName(RpcType.service.name());
		methods.add(methodConfig);

		methodConfig = new MethodConfig();
		methodConfig.setName(RpcType.oneway.name());
		methodConfig.setAsync(true);
		methodConfig.setSent(true);
		methods.add(methodConfig);

		referenceConfig.setMethods(methods);

		return referenceConfig;
	}

	/**
	 * 清理ReferenceConfig
	 */
	private void destroyReferenceConfig(Map<String, ReferenceConfig<GatewayService<?, ?>>> referenceConfigMap) {
		for (ReferenceConfig<GatewayService<?, ?>> item : referenceConfigMap.values()) {
			item.destroy();
		}
		referenceConfigMap.clear();
	}

	/**
	 * 取得服务分组
	 * 
	 * @param gatewayServiceConfig
	 * @return
	 */
	private String getGroup(GatewayServiceConfig gatewayServiceConfig) {
		return systemName + "-" + gatewayServiceConfig.getSystemName();
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

	/**
	 * @param registryConfig
	 *            the registryConfig to set
	 */
	public void setRegistryConfig(RegistryConfig registryConfig) {
		this.registryConfig = registryConfig;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
