/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.biz.shared.notifyer.MessageListener;
import com.xianglin.gateway.core.service.repository.GroovyScriptExecutorRepository;

/**
 * 刷新Groovy脚本消息监听器
 * 
 * @author pengpeng 2016年2月25日下午9:15:25
 */
public class RefreshGroovyScriptMessageListener implements MessageListener {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(RefreshGroovyScriptMessageListener.class);

	/** groovyScriptExecutorRepository */
	private GroovyScriptExecutorRepository groovyScriptExecutorRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.notifyer.MessageListener#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String notifyMessage) {
		try {
			groovyScriptExecutorRepository.refresh();
		} catch (Exception e) {
			logger.error("refreshGroovyScript error!", e);
		}
		logger.info("refreshGroovyScript over!");
	}

	/**
	 * @param groovyScriptExecutorRepository
	 *            the groovyScriptExecutorRepository to set
	 */
	public void setGroovyScriptExecutorRepository(GroovyScriptExecutorRepository groovyScriptExecutorRepository) {
		this.groovyScriptExecutorRepository = groovyScriptExecutorRepository;
	}

}
