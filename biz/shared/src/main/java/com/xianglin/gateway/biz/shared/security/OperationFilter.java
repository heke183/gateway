/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;

/**
 * operation安全过滤器
 * 
 * @author pengpeng 2016年1月19日下午9:39:41
 */
public class OperationFilter implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(OperationFilter.class);

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		GatewayServiceConfig config = gatewayServiceConfigRepository.get(gatewayRequest.getOperation());
		if (config == null) {
			logger.warn("not support operation! gatewayRequest:{}", gatewayRequest);
			gatewayServiceConfigRepository.refresh();//刷新缓存
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.OperationTypeMissed));
			return false;
		}
		return true;
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

}
