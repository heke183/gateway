package com.xianglin.gateway.common.dal.daointerface;

import java.util.List;

import com.xianglin.gateway.common.dal.dataobject.GatewayServiceConfigDO;

/**
 * 网关业务服务方法配置DAO
 * 
 * @author pengpeng 2016年1月20日下午4:04:51
 */
public interface GatewayServiceConfigDAO {

	/**
	 * 根据网关名称取得所有启用状态的网关业务服务配置DO
	 * 
	 * @param gatewayName
	 * @return
	 */
	List<GatewayServiceConfigDO> getAllEnable(String gatewayName);

}
