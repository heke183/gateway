/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;

/**
 * 安全过滤器链
 * 
 * @author pengpeng 2016年1月19日下午9:11:35
 */
public class SecurityFilterChain implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SecurityFilterChain.class);

	/** 安全过滤器列表 */
	private List<SecurityFilter> securityFilterList = new ArrayList<SecurityFilter>();

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		try {
			for (SecurityFilter filter : securityFilterList) {
				if (!filter.doFilter(gatewayRequest, gatewayResponse)) {
					// 如果不再需要继续执行，则中断返回
					return false;
				}
			}
			return true;
		} catch (Throwable e) {
			logger.error("security filter error! gatewayRequest:" + gatewayRequest, e);
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.GATEWAY_ERROR));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.UnknowError));
			return false;
		}
	}

	/**
	 * @param securityFilterList
	 *            the securityFilterList to set
	 */
	public void setSecurityFilterList(List<SecurityFilter> securityFilterList) {
		this.securityFilterList = securityFilterList;
	}

}
