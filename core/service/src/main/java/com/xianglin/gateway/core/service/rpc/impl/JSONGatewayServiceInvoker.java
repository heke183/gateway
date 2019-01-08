/**
 * 
 */
package com.xianglin.gateway.core.service.rpc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.GatewayService;
import com.xianglin.gateway.common.service.spi.JSONGatewayService;
import com.xianglin.gateway.common.service.spi.exception.ServiceMethodNotFoundException;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceRequest;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.GatewayRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Json格式网关服务调用器
 * 
 * @author pengpeng 2016年1月13日下午3:06:39
 */
public class JSONGatewayServiceInvoker extends BaseGatewayServiceInvoker {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(JSONGatewayServiceInvoker.class);

	/**
	 * @see com.xianglin.gateway.core.service.rpc.impl.BaseGatewayServiceInvoker#callService(com.xianglin.gateway.common.service.spi.GatewayService,
	 *      com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig)
	 */
	@Override
	protected ServiceResponse<?> callService(GatewayService<?, ?> gatewayService, GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig) {
		if (!(gatewayService instanceof JSONGatewayService)) {
			logger.error("illegal gatewayService interface type:{}", gatewayService.getClass());
			// return new ServiceResponse<>(ResponseEnum.GATEWAY_ERROR);
			return new ServiceResponse<>(ResultEnum.UnknowError);
		}

		JSONGatewayService jsonGatewayService = (JSONGatewayService) gatewayService;
		ServiceRequest<String> serviceRequest = createJsonServiceRequest(gatewayRequest, gatewayServiceConfig);

		ServiceResponse<?> serviceResponse = null;
		try {
			serviceResponse = jsonGatewayService.service(serviceRequest);
		} catch (Throwable e) {
			logger.error("call service error! ", e);
			if (e instanceof ServiceMethodNotFoundException) {
				// serviceResponse = new
				// ServiceResponse<>(ResponseEnum.GATEWAY_ERROR);
				serviceResponse = new ServiceResponse<>(ResultEnum.UnknowError);
			} else {
				// serviceResponse = new
				// ServiceResponse<>(ResponseEnum.RPC_ERROR);
				serviceResponse = new ServiceResponse<>(ResultEnum.RemoteAccessException);
			}
		}
		if (serviceResponse == null) {
			// serviceResponse = new
			// ServiceResponse<>(ResponseEnum.SERVICE_ERROR);
			serviceResponse = new ServiceResponse<>(ResultEnum.BizException);
		}
		return serviceResponse;
	}

	/**
	 * @see com.xianglin.gateway.core.service.rpc.impl.BaseGatewayServiceInvoker#callOneway(com.xianglin.gateway.common.service.spi.GatewayService,
	 *      com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig)
	 */
	@Override
	protected void callOneway(GatewayService<?, ?> gatewayService, GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig) {
		if (!((gatewayServiceConfig.getGatewayServiceInterface().equals(JSONGatewayService.class)))) {
			logger.error("illegal gatewayService interface type:{}", gatewayService.getClass());
			return;
		}

		JSONGatewayService jsonGatewayService = (JSONGatewayService) gatewayService;
		ServiceRequest<String> serviceRequest = createJsonServiceRequest(gatewayRequest, gatewayServiceConfig);

		try {
			jsonGatewayService.oneway(serviceRequest);
		} catch (Throwable e) {
			logger.error("call oneway error! ", e);
		}
	}

	/**
	 * 创建JSON类型的ServiceRequest
	 * 
	 * @param gatewayRequest
	 * @param gatewayServiceConfig
	 * @return
	 */
	private ServiceRequest<String> createJsonServiceRequest(GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig) {
		ServiceRequest<String> serviceRequest = new ServiceRequest<String>();
		Session session = gatewayRequest.getSession();
		if (session != null) {
			serviceRequest.setSessionId(session.getId());
		}
		serviceRequest.setServiceId(gatewayServiceConfig.getServiceId());
		@SuppressWarnings("unchecked")
		GatewayRequest<String> jsonGatewayRequest = (GatewayRequest<String>) gatewayRequest;
		serviceRequest.setRequestData(jsonGatewayRequest.getRequestData());
		Map<String, Object> properties = new HashMap<>();
		for(Map.Entry<String,String> entry : gatewayRequest.getParamMap().entrySet()){
			properties.put(entry.getKey(),entry.getValue());
		}
		serviceRequest.setProperties(properties);
		return serviceRequest;
	}

}
