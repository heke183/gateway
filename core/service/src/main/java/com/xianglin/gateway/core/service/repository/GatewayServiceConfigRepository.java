/**
 * 
 */
package com.xianglin.gateway.core.service.repository;

import java.util.List;

import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;

/**
 * 网关服务配置信息仓储
 * 
 * @author pengpeng 2015年4月27日下午12:01:48
 */
public interface GatewayServiceConfigRepository {

	/**
	 * 启动重新加载
	 */
	void refresh();

	/**
	 * 回滚重新加载
	 * 
	 */
	void rollback();

	/**
	 * 完成重新加载
	 * 
	 */
	void complete();

	/**
	 * 根据operation取得对应的网关服务方法配置信息
	 * 
	 * @param alias
	 * @return
	 */
	GatewayServiceConfig get(String operation);

	/**
	 * 取得所有网关服务方法配置
	 * 
	 * @return
	 */
	List<GatewayServiceConfig> getAll();
}
