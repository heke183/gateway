/**
 * 
 */
package com.xianglin.gateway.core.service.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.xianglin.gateway.common.service.spi.GatewayService;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;
import com.xianglin.gateway.core.service.repository.GatewayServiceRepository;
import com.xianglin.gateway.core.service.repository.ReferenceConfigRepository;

/**
 * 远程GatewayService代理仓储实现类
 * 
 * @author pengpeng 2015年4月27日下午12:13:31
 */
public class GatewayServiceRepositoryImpl implements GatewayServiceRepository, InitializingBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceRepositoryImpl.class);

	/** gatewayServiceCache */
	private Map<String, GatewayService<?, ?>> cache = new HashMap<String, GatewayService<?, ?>>();

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/** referenceConfigRepository */
	private ReferenceConfigRepository referenceConfigRepository;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		refresh();
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceRepository#refresh()
	 */
	@Override
	public void refresh() {
		synchronized (this) {
			try {
				gatewayServiceConfigRepository.refresh();
				referenceConfigRepository.refresh();

				Map<String, GatewayService<?, ?>> map = new HashMap<String, GatewayService<?, ?>>();
				List<GatewayServiceConfig> gatewayServiceConfigList = gatewayServiceConfigRepository.getAll();
				Map<String, ReferenceConfig<GatewayService<?, ?>>> referenceConfigMap = referenceConfigRepository
						.getAll();
				for (GatewayServiceConfig gatewayServiceConfig : gatewayServiceConfigList) {
					GatewayService<?, ?> gatewayService = map.get(gatewayServiceConfig.getReferenceConfigId());
					if (gatewayService != null) {
						continue;
					}
					gatewayService = createGatewayService(gatewayServiceConfig,
							referenceConfigMap.get(gatewayServiceConfig.getReferenceConfigId()));
					map.put(gatewayServiceConfig.getReferenceConfigId(), gatewayService);
				}
				cache = map;
				referenceConfigRepository.complete();
				gatewayServiceConfigRepository.complete();

				logger.info("gatewayService refresh over!");
			} catch (Throwable e) {
				logger.info("gatewayService refresh fail!");
				referenceConfigRepository.rollback();
				gatewayServiceConfigRepository.rollback();
			}
		}
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GatewayServiceRepository#get(java.lang.String)
	 */
	@Override
	public GatewayService<?, ?> get(String key) {
		return cache.get(key);
	}

	/**
	 * 创建网关服务本地代理
	 * 
	 * @param methodConfig
	 * @return
	 */
	private GatewayService<?, ?> createGatewayService(GatewayServiceConfig gatewayServiceConfig,
			ReferenceConfig<GatewayService<?, ?>> referenceConfig) {
		if (referenceConfig == null) {
			throw new RuntimeException("can not find referenceConfig for gatewayServiceConfig:" + gatewayServiceConfig);
		}
		GatewayService<?, ?> gatewayService = null;
		try {
			gatewayService = referenceConfig.get();
		} catch (Throwable e) {
			logger.error("create GatewayService error! gatewayServiceConfig:" + gatewayServiceConfig, e);
			throw new RuntimeException(e);
		}
		logger.info("create GatewayService success! gatewayServiceConfig:{}", gatewayServiceConfig);

		return gatewayService;
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

	/**
	 * @param referenceConfigRepository
	 *            the referenceConfigRepository to set
	 */
	public void setReferenceConfigRepository(ReferenceConfigRepository referenceConfigRepository) {
		this.referenceConfigRepository = referenceConfigRepository;
	}

}
