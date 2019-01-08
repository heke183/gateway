/**
 * 
 */
package com.xianglin.gateway.biz.shared.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.fala.session.SessionRepository;
import com.xianglin.gateway.biz.shared.PreProcessor;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;

/**
 * 更换Session前置处理器
 * 
 * @author pengpeng 2016年1月19日下午9:02:56
 */
public class ChangeSessionPreProcessor implements PreProcessor {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ChangeSessionPreProcessor.class);

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/** sessionRepository */
	private SessionRepository<Session> sessionRepository;

	/**
	 * @see com.pingan.gateway.biz.shared.PreProcessor#preProcess(com.pingan.gateway.core.model.GatewayRequest)
	 */
	@Override
	public void preProcess(GatewayRequest<?> gatewayRequest) {
		GatewayServiceConfig config = gatewayServiceConfigRepository.get(gatewayRequest.getOperation());
		if (config.isChangeSessionBeforeInvoke()) {
			Session newSession = sessionRepository.createSession();
			Session oldSession = gatewayRequest.getSession();
			Set<String> attributeNames = oldSession.getAttributeNames();
			for (String attributeName : attributeNames) {
				newSession.setAttribute(attributeName, oldSession.getAttribute(attributeName));
			}
			sessionRepository.save(newSession);
			sessionRepository.delete(oldSession.getId());
			gatewayRequest.setSession(newSession);

			logger.debug("change session success! newSessionId:{},oldSessionId:{}", newSession.getId(),
					oldSession.getId());
		}
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

	/**
	 * @param sessionRepository
	 *            the sessionRepository to set
	 */
	public void setSessionRepository(SessionRepository<Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

}
