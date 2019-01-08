/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

/**
 * 基于Protobuf协议的网关服务接口
 * 
 * @author pengpeng 2016年1月20日下午9:22:17
 */
public interface ProtobufGatewayService extends GatewayService<byte[][], byte[]> {

	/** 网关服务接口版本 */
	public static final String VERSION = "1.0.0";
}
