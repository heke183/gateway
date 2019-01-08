/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.biz.shared.notifyer.MessageListener;
import com.xianglin.gateway.core.service.repository.GatewayServiceRepository;

/**
 * 网关服务配置更新消息监听器
 * 
 * @author pengpeng 2016年1月21日上午11:47:25
 */
public class RefreshGatewayServiceMessageListener implements MessageListener {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(RefreshGatewayServiceMessageListener.class);

	/** gatewayServiceRepository */
	private GatewayServiceRepository gatewayServiceRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.notifyer.MessageListener#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String notifyMessage) {
		try {
			gatewayServiceRepository.refresh();
		} catch (Exception e) {
			logger.error("refreshGatewayConfig error!", e);
		}
		logger.info("refreshGatewayConfig over!");
	}

	/**
	 * @param gatewayServiceRepository
	 *            the gatewayServiceRepository to set
	 */
	public void setGatewayServiceRepository(GatewayServiceRepository gatewayServiceRepository) {
		this.gatewayServiceRepository = gatewayServiceRepository;
	}

}
