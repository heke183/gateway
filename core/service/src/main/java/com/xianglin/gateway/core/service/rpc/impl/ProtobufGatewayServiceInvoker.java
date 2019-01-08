/**
 * 
 */
package com.xianglin.gateway.core.service.rpc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.GatewayService;
import com.xianglin.gateway.common.service.spi.ProtobufGatewayService;
import com.xianglin.gateway.common.service.spi.exception.ServiceMethodNotFoundException;
import com.xianglin.gateway.common.service.spi.impl.JSONGatewayServiceImpl;
import com.xianglin.gateway.common.service.spi.impl.ProtobufGatewayServiceImpl;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceRequest;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.GatewayRequest;

/**
 * Protobuf格式的网管服务调用器
 * 
 * @author pengpeng 2016年1月13日下午3:23:32
 */
public class ProtobufGatewayServiceInvoker extends BaseGatewayServiceInvoker {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ProtobufGatewayServiceInvoker.class);

	/**
	 * @see com.xianglin.gateway.core.service.rpc.impl.BaseGatewayServiceInvoker#callService(com.xianglin.gateway.common.service.spi.GatewayService,
	 *      com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig)
	 */
	@Override
	protected ServiceResponse<?> callService(GatewayService<?, ?> gatewayService, GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig) {
		if (!(gatewayService instanceof ProtobufGatewayService)) {
			logger.error("illegal gatewayService interface type:{}", gatewayService.getClass());
			// return new ServiceResponse<>(ResponseEnum.GATEWAY_ERROR);
			return new ServiceResponse<>(ResultEnum.UnknowError);
		}

		ProtobufGatewayService protobufGatewayService = (ProtobufGatewayService) gatewayService;
		ServiceRequest<byte[][]> serviceRequest = createProtobufServiceRequest(gatewayRequest, gatewayServiceConfig);

		ServiceResponse<?> serviceResponse = null;
		try {
			serviceResponse = protobufGatewayService.service(serviceRequest);
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
		if (!(gatewayService instanceof JSONGatewayServiceImpl)) {
			logger.error("illegal gatewayService interface type:{}", gatewayService.getClass());
			return;
		}
		ProtobufGatewayServiceImpl protobufGatewayService = (ProtobufGatewayServiceImpl) gatewayService;
		ServiceRequest<byte[][]> serviceRequest = createProtobufServiceRequest(gatewayRequest, gatewayServiceConfig);

		try {
			protobufGatewayService.oneway(serviceRequest);
		} catch (Throwable e) {
			logger.error("call oneway error! ", e);
		}
	}

	/**
	 * 创建Protobuf类型的ServiceRequest
	 * 
	 * @param gatewayRequest
	 * @param gatewayServiceConfig
	 * @return
	 */
	private ServiceRequest<byte[][]> createProtobufServiceRequest(GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig) {
		ServiceRequest<byte[][]> serviceRequest = new ServiceRequest<byte[][]>();
		Session session = gatewayRequest.getSession();
		if (session != null) {
			serviceRequest.setSessionId(session.getId());
		}
		serviceRequest.setServiceId(gatewayServiceConfig.getServiceId());
		@SuppressWarnings("unchecked")
		GatewayRequest<byte[][]> protobufGatewayRequest = (GatewayRequest<byte[][]>) gatewayRequest;
		serviceRequest.setRequestData(protobufGatewayRequest.getRequestData());
		return serviceRequest;
	}

}
