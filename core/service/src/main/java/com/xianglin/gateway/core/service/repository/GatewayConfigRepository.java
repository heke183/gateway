/**
 * 
 */
package com.xianglin.gateway.core.service.repository;

/**
 * 网关配置项仓储
 * 
 * @author pengpeng 2016年1月20日下午5:09:10
 */
public interface GatewayConfigRepository {

	/**
	 * 从数据库中重新读取配置
	 * 
	 */
	void refresh();

	/**
	 * 根据配置项key取得对应的配置项value，如果value为空，则返回defaultValue
	 * 
	 * @param key
	 * @return
	 */
	String get(String key, String defaultValue);

	/**
	 * 根据配置项key和类型取得对应的配置项值，如果value为空，则返回defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 * @param clazz
	 * @return
	 */
	<T> T get(String key, T defaultValue, Class<T> clazz);
}
