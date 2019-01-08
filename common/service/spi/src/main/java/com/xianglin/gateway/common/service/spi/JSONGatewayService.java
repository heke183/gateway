/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

/**
 * 基于JSON协议的网关服务接口
 * 
 * @author pengpeng 2016年1月20日下午9:17:05
 */
public interface JSONGatewayService extends GatewayService<String, String> {

	/** 网关服务接口版本 */
	public static final String VERSION = "1.0.0";
}
