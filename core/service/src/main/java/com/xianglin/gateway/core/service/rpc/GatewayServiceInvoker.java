/**
 * 
 */
package com.xianglin.gateway.core.service.rpc;

import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;

/**
 * 远程GatewayService调用器
 * 
 * @author pengpeng 2015年4月27日下午12:21:57
 */
public interface GatewayServiceInvoker {

	/**
	 * 调用远程网关服务方法
	 * 
	 * @param gatewayRequest
	 * @return
	 */
	void invoke(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse);

}
