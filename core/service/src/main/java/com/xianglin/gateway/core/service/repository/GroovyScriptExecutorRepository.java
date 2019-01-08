/**
 * 
 */
package com.xianglin.gateway.core.service.repository;

import com.xianglin.gateway.common.util.GroovyScriptExecutor;

/**
 * Groovy脚本执行器仓储
 * 
 * @author pengpeng 2016年2月25日下午7:07:18
 */
public interface GroovyScriptExecutorRepository {

	/**
	 * 启动重新加载
	 */
	void refresh();

	/**
	 * 根据operation取得对应的Groovy脚本执行器
	 * 
	 * @param operation
	 * @return
	 */
	GroovyScriptExecutor get(String operation);

}
