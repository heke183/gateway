/**
 * 
 */
package com.xianglin.gateway.common.dal.dataobject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 网关服务配置DO
 * 
 * @author pengpeng 2015年4月28日下午2:38:54
 */
public class GatewayServiceConfigDO extends BaseDO {

	/** 服务别名 */
	private String alias;

	/** 服务提供者系统名称 */
	private String systemName;

	/** 服务唯一标识，格式：服务接口全限定名.方法名 */
	private String serviceId;

	/** 方法类型 */
	private String rpcType;

	/** 服务提供者使用的网关服务接口版本 */
	private String spiVersion;

	/** 服务协议 */
	private String protocol;

	/** 网关服务接口名称 */
	private String gatewayServiceInterfaceName;

	/** 是否只有登录后才能调用 */
	private boolean needLogin;

	/** 调用服务前是否需要更换session */
	private boolean changeSession;

	/** 调用服务后是否需要删除session */
	private boolean deleteSession;

	/** 是否启用ETag做响应缓存 */
	private boolean useETag;

	/** 服务调用超时时间 */
	private Integer timeout;

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the rpcType
	 */
	public String getRpcType() {
		return rpcType;
	}

	/**
	 * @param rpcType
	 *            the rpcType to set
	 */
	public void setRpcType(String rpcType) {
		this.rpcType = rpcType;
	}

	/**
	 * @return the spiVersion
	 */
	public String getSpiVersion() {
		return spiVersion;
	}

	/**
	 * @param spiVersion
	 *            the spiVersion to set
	 */
	public void setSpiVersion(String spiVersion) {
		this.spiVersion = spiVersion;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the gatewayServiceInterfaceName
	 */
	public String getGatewayServiceInterfaceName() {
		return gatewayServiceInterfaceName;
	}

	/**
	 * @param gatewayServiceInterfaceName
	 *            the gatewayServiceInterfaceName to set
	 */
	public void setGatewayServiceInterfaceName(String gatewayServiceInterfaceName) {
		this.gatewayServiceInterfaceName = gatewayServiceInterfaceName;
	}

	/**
	 * @return the needLogin
	 */
	public boolean isNeedLogin() {
		return needLogin;
	}

	/**
	 * @param needLogin
	 *            the needLogin to set
	 */
	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	/**
	 * @return the changeSession
	 */
	public boolean isChangeSession() {
		return changeSession;
	}

	/**
	 * @param changeSession
	 *            the changeSession to set
	 */
	public void setChangeSession(boolean changeSession) {
		this.changeSession = changeSession;
	}

	/**
	 * @return the deleteSession
	 */
	public boolean isDeleteSession() {
		return deleteSession;
	}

	/**
	 * @param deleteSession
	 *            the deleteSession to set
	 */
	public void setDeleteSession(boolean deleteSession) {
		this.deleteSession = deleteSession;
	}

	/**
	 * @return the useETag
	 */
	public boolean isUseETag() {
		return useETag;
	}

	/**
	 * @param useETag
	 *            the useETag to set
	 */
	public void setUseETag(boolean useETag) {
		this.useETag = useETag;
	}

	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
