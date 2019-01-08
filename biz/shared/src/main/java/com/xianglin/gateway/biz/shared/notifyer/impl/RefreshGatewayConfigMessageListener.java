/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.biz.shared.notifyer.MessageListener;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;

/**
 * 网关配置更新消息监听器
 * 
 * @author pengpeng 2016年1月21日上午11:41:47
 */
public class RefreshGatewayConfigMessageListener implements MessageListener {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(RefreshGatewayConfigMessageListener.class);

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.notifyer.MessageListener#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String notifyMessage) {
		try {
			gatewayConfigRepository.refresh();
		} catch (Exception e) {
			logger.error("refreshGatewayConfig error!", e);
		}
		logger.info("refreshGatewayConfig over!");
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

}
