/**
 * 
 */
package com.xianglin.gateway.biz.shared.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.biz.shared.GatewayServiceProcessor;
import com.xianglin.gateway.biz.shared.PostProcessor;
import com.xianglin.gateway.biz.shared.PreProcessor;
import com.xianglin.gateway.biz.shared.security.SecurityFilter;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.rpc.GatewayServiceInvoker;

/**
 * 网关业务处理器实现类
 * 
 * @author pengpeng 2016年1月19日下午9:03:31
 */
public class GatewayServiceProcessorImpl implements GatewayServiceProcessor {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceProcessorImpl.class);

	/** preProcessor */
	private PreProcessor preProcessor;

	/** postRrocessor */
	private PostProcessor postProcessor;

	/** gatewayServiceInvoker */
	private GatewayServiceInvoker gatewayServiceInvoker;

	/** securityCheckerList */
	private SecurityFilter securityFilterChain;

	/**
	 * @see com.pingan.gateway.biz.shared.GatewayServiceProcessor#process(com.pingan.gateway.core.model.GatewayRequest)
	 */
	@Override
	public void process(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		if (!securityFilterChain.doFilter(gatewayRequest, gatewayResponse)) {
			return;
		}

		try {
			preProcessor.preProcess(gatewayRequest);

			gatewayServiceInvoker.invoke(gatewayRequest, gatewayResponse);

			postProcessor.postProces(gatewayRequest);
		} catch (Throwable e) {
			logger.error("process error!", e);
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.GATEWAY_ERROR));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.UnknowError));
		}
	}

	/**
	 * @param securityFilterChain
	 *            the securityFilterChain to set
	 */
	public void setSecurityFilterChain(SecurityFilter securityFilterChain) {
		this.securityFilterChain = securityFilterChain;
	}

	/**
	 * @param gatewayServiceInvoker
	 *            the gatewayServiceInvoker to set
	 */
	public void setGatewayServiceInvoker(GatewayServiceInvoker gatewayServiceInvoker) {
		this.gatewayServiceInvoker = gatewayServiceInvoker;
	}

	/**
	 * @param preProcessor
	 *            the preProcessor to set
	 */
	public void setPreProcessor(PreProcessor preProcessor) {
		this.preProcessor = preProcessor;
	}

	/**
	 * @param postProcessor
	 *            the postProcessor to set
	 */
	public void setPostProcessor(PostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}

}
