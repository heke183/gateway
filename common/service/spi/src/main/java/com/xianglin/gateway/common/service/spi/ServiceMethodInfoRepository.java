/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

import java.util.List;

import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceMethodInfo;

/**
 * 网关服务方法仓库
 * 
 * @author pengpeng 2015年9月6日下午2:47:54
 */
public interface ServiceMethodInfoRepository {

	/**
	 * 根据指定的服务唯一标识取得对应的methodInfo对象
	 * 
	 * @param serviceId
	 * @return
	 */
	ServiceMethodInfo get(String serviceId);

	/**
	 * 取得所有网关服务列表
	 * 
	 * @return
	 */
	List<GatewayServiceConfig> getServiceMetaInfoList();

}