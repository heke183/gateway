/**
 * 
 */
package com.xianglin.gateway.core.service.rpc.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.util.GroovyScriptExecutor;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;
import com.xianglin.gateway.core.service.repository.GroovyScriptExecutorRepository;
import com.xianglin.gateway.core.service.rpc.GatewayServiceInvoker;

/**
 * GatewayServiceInvoker包装器
 * 
 * @author pengpeng 2016年2月25日下午5:27:03
 */
public class GatewayServiceInvokerWrapper implements GatewayServiceInvoker {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceInvokerWrapper.class);

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/** groovyScriptExecutor */
	private GroovyScriptExecutorRepository groovyScriptExecutorRepository;

	/** gatewayServiceInvoker */
	private GatewayServiceInvoker gatewayServiceInvoker;

	/**
	 * @see com.xianglin.gateway.core.service.rpc.GatewayServiceInvoker#invoke(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public void invoke(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		boolean useGroovy = gatewayConfigRepository.get(Constants.SWITCHER_USE_GROOVY, false, Boolean.class);
		if (!useGroovy) {
			gatewayServiceInvoker.invoke(gatewayRequest, gatewayResponse);
			return;
		}

		GroovyScriptExecutor groovyScriptExecutor = groovyScriptExecutorRepository.get(gatewayRequest.getOperation());
		if (groovyScriptExecutor == null) {
			gatewayServiceInvoker.invoke(gatewayRequest, gatewayResponse);
			return;
		}

		Map<String, Object> bindingMap = new HashMap<String, Object>();
		bindingMap.put(Constants.GROOVY_GATEWAY_REQUEST, gatewayRequest);
		bindingMap.put(Constants.GROOVY_GATEWAY_RESPONSE, gatewayResponse);
		bindingMap.put(Constants.GROOVY_GATEWAY_SERVICE_INVOKER, gatewayServiceInvoker);
		bindingMap.put(Constants.GROOVY_LOGGER, logger);

		groovyScriptExecutor.execute(bindingMap);
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

	/**
	 * @param gatewayServiceInvoker
	 *            the gatewayServiceInvoker to set
	 */
	public void setGatewayServiceInvoker(GatewayServiceInvoker gatewayServiceInvoker) {
		this.gatewayServiceInvoker = gatewayServiceInvoker;
	}

	/**
	 * @param groovyScriptExecutorRepository
	 *            the groovyScriptExecutorRepository to set
	 */
	public void setGroovyScriptExecutorRepository(GroovyScriptExecutorRepository groovyScriptExecutorRepository) {
		this.groovyScriptExecutorRepository = groovyScriptExecutorRepository;
	}

}
