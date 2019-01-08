/**
 * 
 */
package com.xianglin.gateway.core.service.repository;

import java.util.Map;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.xianglin.gateway.common.service.spi.GatewayService;

/**
 * Dubbo ReferenceConfig 仓储
 * 
 * @author pengpeng 2016年1月20日下午6:57:38
 */
public interface ReferenceConfigRepository {

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
	 * 根据key取得对应的Dubbo ReferenceConfig
	 * 
	 * @param alias
	 * @return
	 */
	ReferenceConfig<GatewayService<?, ?>> get(String key);

	/**
	 * 取得所有Dubbo ReferenceConfig
	 * 
	 * @return
	 */
	Map<String, ReferenceConfig<GatewayService<?, ?>>> getAll();
}
