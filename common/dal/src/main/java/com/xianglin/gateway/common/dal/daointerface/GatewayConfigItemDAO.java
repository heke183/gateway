/**
 * 
 */
package com.xianglin.gateway.common.dal.daointerface;

import java.util.List;

import com.xianglin.gateway.common.dal.dataobject.GatewayConfigItemDO;

/**
 * 网关配置信息DAO
 * 
 * @author pengpeng 2016年1月20日下午4:04:19
 */
public interface GatewayConfigItemDAO {

	/**
	 * 根据网关名称取得所有启动状态的网关配置项
	 * 
	 * @param gatewayName
	 * @return
	 */
	List<GatewayConfigItemDO> getAllEnable(String gatewayName);
}
