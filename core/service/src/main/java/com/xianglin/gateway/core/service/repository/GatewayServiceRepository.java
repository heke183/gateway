/**
 * 
 */
package com.xianglin.gateway.core.service.repository;

import com.xianglin.gateway.common.service.spi.GatewayService;

/**
 * 远程GatewayService代理仓储
 * 
 * @author pengpeng 2015年4月27日上午11:59:11
 */
public interface GatewayServiceRepository {

	/**
	 * 刷新，重新根据配置信息生成远程GatewayService代理，销毁原有代理
	 */
	void refresh();

	/**
	 * 根据key值获取远程GatewayService代理
	 * 
	 * @param key
	 * @return
	 */
	GatewayService<?, ?> get(String key);

}
