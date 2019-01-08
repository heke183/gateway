package com.xianglin.gateway.core.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xianglin.fala.session.Session;

/**
 * 网关请求
 * 
 * @author pengpeng 2015年4月27日下午2:06:10
 */
public class GatewayRequest<T> {

	/** 设备唯一标识 */
	private String deviceId;

	/** operation */
	private String operation;

	/** operationData */
	private T requestData;

	/** session */
	private Session session;

	/** 请求参数 */
	private Map<String, String> paramMap = new HashMap<String, String>();

	/**
	 * 根据key取得请求参数
	 * 
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		return paramMap.get(key);
	}

	/**
	 * 设置请求参数
	 * 
	 * @param key
	 * @param value
	 */
	public void setParam(String key, String value) {
		paramMap.put(key, value);
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
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
	 * @return the paramMap
	 */
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @param paramMap
	 *            the paramMap to set
	 */
	public void setParamMap(Map<String, String> paramMap) {
		if (paramMap == null) {
			throw new RuntimeException("paramMap can not be null!");
		}
		this.paramMap = paramMap;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
