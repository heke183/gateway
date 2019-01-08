/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import com.xianglin.gateway.common.service.spi.JSONGatewayService;

/**
 * 基于JSON协议的网关服务实现类
 * 
 * @author pengpeng 2015年11月25日上午11:37:32
 */
public class JSONGatewayServiceImpl extends BaseGatewayService<String, String> implements JSONGatewayService {

	/**
	 * @see com.xianglin.gateway.common.service.spi.GatewayService#getVersion()
	 */
	@Override
	public String getVersion() {
		return JSONGatewayService.VERSION;
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.impl.BaseGatewayService#getGatewayServiceInterface()
	 */
	@Override
	public Class<?> getGatewayServiceInterface() {
		return JSONGatewayService.class;
	}

	/**
	 * 构造方法，使用json参数转换器和json响应转换器
	 */
	public JSONGatewayServiceImpl() {
		this.paramProcessor = new JSONParamProcessor();
		this.responseProcessor = new JSONResponseProcessor();
	}

}
