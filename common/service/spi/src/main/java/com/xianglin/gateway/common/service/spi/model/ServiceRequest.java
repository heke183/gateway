/**
 * 
 */
package com.xianglin.gateway.common.service.spi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 服务请求
 * 
 * @author pengpeng
 */
public class ServiceRequest<T> implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -7528281073517444392L;

	/** 服务唯一标识，标识要调用哪个服务 */
	private String serviceId;

	/** 客户端经由网关透传的服务请求参数信息 */
	private T requestData;

	/** 用户的sessionId */
	private String sessionId;

	/** 请求的属性，扩展信息 */
	private Map<String, Object> properties = new HashMap<String, Object>();

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
	 * @return the requestData
	 */
	public T getRequestData() {
		return requestData;
	}

	/**
	 * @param requestData
	 *            the requestData to set
	 */
	public void setRequestData(T requestData) {
		this.requestData = requestData;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		if (properties == null) {
			throw new RuntimeException("properties can not be null!");
		}
		this.properties = properties;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
