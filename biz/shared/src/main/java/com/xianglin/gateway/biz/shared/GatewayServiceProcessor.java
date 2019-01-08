/**
 * 
 */
package com.xianglin.gateway.biz.shared;

import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;

/**
 * 网关业务处理器
 * 
 * @author pengpeng 2016年1月19日下午9:01:28
 */
public interface GatewayServiceProcessor {

	/**
	 * 处理网关业务
	 * 
	 * @param gatewayRequest
	 * @return
	 */
	void process(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse);
}
