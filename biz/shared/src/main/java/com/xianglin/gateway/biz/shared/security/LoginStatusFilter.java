/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;

/**
 * 登录状态过滤器
 * 
 * @author pengpeng 2016年1月20日下午1:48:49
 */
public class LoginStatusFilter implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(LoginStatusFilter.class);

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		GatewayServiceConfig config = gatewayServiceConfigRepository.get(gatewayRequest.getOperation());
		if (!config.isNeedLoginBeforeInvoke()) {
			// 调用前不需要先登录
			return true;
		}
		Session session = gatewayRequest.getSession();
		if (session.getAttribute(Constants.USER_INFO) == null) {
			logger.info("need login! gatewayRequest：{}", gatewayRequest);
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.NEED_LOGIN));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.SessionStatus));
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
