/**
 * 
 */
package com.xianglin.gateway.common.service.spi.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xianglin.gateway.common.service.spi.model.enums.Protocol;
import com.xianglin.gateway.common.service.spi.model.enums.RpcType;

/**
 * 网关服务配置元数据
 * 
 * @author pengpeng 2015年9月8日下午1:57:02
 */
public class GatewayServiceConfig implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -4093136661825350322L;

	/** 服务别名 */
	private String alias;

	/** 服务提供者系统名称 */
	private String systemName;

	/** 服务唯一标识，格式：服务接口全限定名.方法名 */
	private String serviceId;

	/** 方法类型 */
	private RpcType rpcType = RpcType.service;

	/** 服务提供者使用的网关服务接口版本 */
	private String spiVersion;

	/** 服务协议 */
	private Protocol protocol;

	/** 网关服务接口名称 */
	private Class<?> gatewayServiceInterface;

	/** 是否只有登录后才能调用 */
	private boolean needLoginBeforeInvoke = true;

	/** 调用服务前是否需要更换session */
	private boolean changeSessionBeforeInvoke = false;

	/** 调用服务后是否需要删除session */
	private boolean deleteSessionAfterInvoke = false;

	/** 是否启用ETag做响应缓存 */
	private boolean useETag = false;

	/** 服务调用超时时间，默认3秒钟 */
	private int timeout = 30000;

	/** 描述/备注信息 */
	private String description;

	/**
	 * 取得Dubbo的ReferenceConfig的唯一标识
	 * 
	 * @return
	 */
	public String getReferenceConfigId() {
		return systemName + spiVersion + timeout;
	}

	/**
	 * @return the rpcType
	 */
	public RpcType getRpcType() {
		return rpcType;
	}

	/**
	 * @param rpcType
	 *            the rpcType to set
	 */
	public void setRpcType(RpcType rpcType) {
		this.rpcType = rpcType;
	}

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
	 * @return the protocol
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
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
	 * @return the needLoginBeforeInvoke
	 */
	public boolean isNeedLoginBeforeInvoke() {
		return needLoginBeforeInvoke;
	}

	/**
	 * @param needLoginBeforeInvoke
	 *            the needLoginBeforeInvoke to set
	 */
	public void setNeedLoginBeforeInvoke(boolean needLoginBeforeInvoke) {
		this.needLoginBeforeInvoke = needLoginBeforeInvoke;
	}

	/**
	 * @return the changeSessionBeforeInvoke
	 */
	public boolean isChangeSessionBeforeInvoke() {
		return changeSessionBeforeInvoke;
	}

	/**
	 * @param changeSessionBeforeInvoke
	 *            the changeSessionBeforeInvoke to set
	 */
	public void setChangeSessionBeforeInvoke(boolean changeSessionBeforeInvoke) {
		this.changeSessionBeforeInvoke = changeSessionBeforeInvoke;
	}

	/**
	 * @return the deleteSessionAfterInvoke
	 */
	public boolean isDeleteSessionAfterInvoke() {
		return deleteSessionAfterInvoke;
	}

	/**
	 * @param deleteSessionAfterInvoke
	 *            the deleteSessionAfterInvoke to set
	 */
	public void setDeleteSessionAfterInvoke(boolean deleteSessionAfterInvoke) {
		this.deleteSessionAfterInvoke = deleteSessionAfterInvoke;
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
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the gatewayServiceInterface
	 */
	public Class<?> getGatewayServiceInterface() {
		return gatewayServiceInterface;
	}

	/**
	 * @param gatewayServiceInterface
	 *            the gatewayServiceInterface to set
	 */
	public void setGatewayServiceInterface(Class<?> gatewayServiceInterface) {
		this.gatewayServiceInterface = gatewayServiceInterface;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
