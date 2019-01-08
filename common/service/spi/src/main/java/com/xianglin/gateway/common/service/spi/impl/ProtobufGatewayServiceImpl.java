/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import com.xianglin.gateway.common.service.spi.ProtobufGatewayService;

/**
 * 基于Protobuf协议的网关服务实现类
 * 
 * @author pengpeng 2016年1月13日上午11:36:44
 */
public class ProtobufGatewayServiceImpl extends BaseGatewayService<byte[][], byte[]> implements ProtobufGatewayService {

	/**
	 * @see com.xianglin.gateway.common.service.spi.GatewayService#getVersion()
	 */
	@Override
	public String getVersion() {
		return ProtobufGatewayService.VERSION;
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.impl.BaseGatewayService#getGatewayServiceInterface()
	 */
	@Override
	public Class<?> getGatewayServiceInterface() {
		return ProtobufGatewayService.class;
	}

}
