/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;

/**
 * 安全过滤器
 * 
 * @author pengpeng 2016年1月19日下午9:07:12
 */
public interface SecurityFilter {

	/**
	 * 执行过滤逻辑
	 * 
	 * @param gatewayRequest
	 * @param gatewayResponse
	 * @return true：继续执行接下来的逻辑，false：不再继续执行接下来的逻辑
	 */
	boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse);

}
