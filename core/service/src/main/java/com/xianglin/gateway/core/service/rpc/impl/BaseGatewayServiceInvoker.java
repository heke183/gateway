/**
 * 
 */
package com.xianglin.gateway.core.service.rpc.impl;

import com.xianglin.gateway.common.service.spi.GatewayService;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.RpcType;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;
import com.xianglin.gateway.core.service.repository.GatewayServiceRepository;
import com.xianglin.gateway.core.service.rpc.GatewayServiceInvoker;

/**
 * 远程网关服务调用器实现类
 * 
 * @author pengpeng 2015年4月27日下午12:22:59
 */
public abstract class BaseGatewayServiceInvoker implements GatewayServiceInvoker {

	/** gatewayServiceRepository */
	protected GatewayServiceRepository gatewayServiceRepository;

	/** gatewayServiceConfigRepository */
	protected GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/**
	 * @see com.xianglin.gateway.core.service.rpc.JSONGatewayServiceInvoker#invoke(com.pingan.gateway.core.model.GatewayRequest)
	 */
	@Override
	public void invoke(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		GatewayServiceConfig gatewayServiceConfig = gatewayServiceConfigRepository.get(gatewayRequest.getOperation());
		if (gatewayServiceConfig == null) {
			throw new RuntimeException("can not find gatewayServiceConfig for request:" + gatewayRequest);
		}
		GatewayService<?, ?> gatewayService = gatewayServiceRepository.get(gatewayServiceConfig.getReferenceConfigId());
		if (gatewayService == null) {
			throw new RuntimeException("can not find gatewayService for gatewayServiceConfig:" + gatewayServiceConfig);
		}

		if (gatewayServiceConfig.getRpcType() == RpcType.service) {
			ServiceResponse<?> serviceResponse = callService(gatewayService, gatewayRequest, gatewayServiceConfig);
			gatewayResponse.setServiceResponse(serviceResponse);
		} else if (gatewayServiceConfig.getRpcType() == RpcType.oneway) {
			callOneway(gatewayService, gatewayRequest, gatewayServiceConfig);
		} else {
			throw new RuntimeException("not support type! gatewayServiceConfig:" + gatewayServiceConfig);
		}
	}

	/**
	 * 调用service方法
	 * 
	 * @param gatewayService
	 * @param gatewayRequest
	 * @param gatewayServiceConfig
	 * @return
	 */
	protected abstract ServiceResponse<?> callService(GatewayService<?, ?> gatewayService,
			GatewayRequest<?> gatewayRequest, GatewayServiceConfig gatewayServiceConfig);

	/**
	 * 调用oneway方法
	 * 
	 * @param gatewayService
	 * @param gatewayRequest
	 * @param gatewayServiceConfig
	 */
	protected abstract void callOneway(GatewayService<?, ?> gatewayService, GatewayRequest<?> gatewayRequest,
			GatewayServiceConfig gatewayServiceConfig);

	/**
	 * @param gatewayServiceRepository
	 *            the gatewayServiceRepository to set
	 */
	public void setGatewayServiceRepository(GatewayServiceRepository gatewayServiceRepository) {
		this.gatewayServiceRepository = gatewayServiceRepository;
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

}
