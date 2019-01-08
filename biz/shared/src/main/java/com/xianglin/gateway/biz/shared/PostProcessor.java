/**
 * 
 */
package com.xianglin.gateway.biz.shared;

import com.xianglin.gateway.core.model.GatewayRequest;

/**
 * 后置处理器
 * 
 * @author pengpeng 2016年1月19日下午9:00:56
 */
public interface PostProcessor {

	/**
	 * 后置处理
	 * 
	 * @param gatewayRequest
	 */
	void postProces(GatewayRequest<?> gatewayRequest);

}
