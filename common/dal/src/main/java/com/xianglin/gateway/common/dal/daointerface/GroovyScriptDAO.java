/**
 * 
 */
package com.xianglin.gateway.common.dal.daointerface;

import java.util.List;

import com.xianglin.gateway.common.dal.dataobject.GroovyScriptDO;

/**
 * Groovy脚本DAO
 * 
 * @author pengpeng 2016年2月25日下午8:23:36
 */
public interface GroovyScriptDAO {

	/**
	 * 根据网关名称取得所有启动状态的groovy脚本
	 * 
	 * @param gatewayName
	 * @return
	 */
	List<GroovyScriptDO> getAllEnable(String gatewayName);
}
