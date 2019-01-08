/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

import java.util.List;

import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceRequest;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;

/**
 * 网关服务SPI接口
 * 
 * @author pengpeng
 */
public interface GatewayService<I, O> {

	/**
	 * 取得服务接口版本
	 * 
	 * @return
	 */
	String getVersion();

	/**
	 * 调用网关服务，同步调用，等待返回结果，用于请求-响应模式
	 * 
	 * @param request
	 * @return
	 */
	ServiceResponse<O> service(ServiceRequest<I> request);

	/**
	 * 调用网关服务，单向异步调用，不等待（不关心）返回结果，用于单向发送模式
	 * 
	 * @param request
	 */
	void oneway(ServiceRequest<I> request);

	/**
	 * 取得所有网关服务配置信息列表，可以调用该方法取得当前系统对网关暴露的所有服务配置信息，可用于在网关系统注册
	 * 
	 * @return
	 */
	List<GatewayServiceConfig> getGatewayServiceConfigList();

}
