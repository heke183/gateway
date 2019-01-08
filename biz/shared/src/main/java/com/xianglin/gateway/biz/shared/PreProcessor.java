/**
 * 
 */
package com.xianglin.gateway.biz.shared;

import com.xianglin.gateway.core.model.GatewayRequest;

/**
 * 前置处理器
 * 
 * @author pengpeng 2016年1月19日下午9:00:42
 */
public interface PreProcessor {

	/**
	 * 前置处理
	 * 
	 * @param gatewayRequest
	 */
	void preProcess(GatewayRequest<?> gatewayRequest);

}
