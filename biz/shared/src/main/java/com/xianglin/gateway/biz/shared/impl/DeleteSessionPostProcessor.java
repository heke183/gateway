/**
 * 
 */
package com.xianglin.gateway.biz.shared.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.biz.shared.PostProcessor;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;

/**
 * 删除session后置处理器
 * 
 * @author pengpeng 2016年1月19日下午9:02:41
 */
public class DeleteSessionPostProcessor implements PostProcessor {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(DeleteSessionPostProcessor.class);

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/**
	 * @see com.pingan.gateway.biz.shared.PostProcessor#postProces(com.pingan.gateway.core.model.GatewayRequest)
	 */
	@Override
	public void postProces(GatewayRequest<?> gatewayRequest) {
		GatewayServiceConfig config = gatewayServiceConfigRepository.get(gatewayRequest.getOperation());
		if (config.isDeleteSessionAfterInvoke()) {
			Session session = gatewayRequest.getSession();
			// 使session过期，在SessionInterceptor中统一删除
			session.setMaxInactiveIntervalInSeconds(0);

			logger.debug("session delete success! sessionId:{}", session.getId());
		}
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

}
