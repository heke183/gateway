package com.xianglin.gateway.common.service.spi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关请求上下文信息，使用threadlocal保存sessionId，请求属性等信息
 * 
 * @author pengpeng 2015年11月26日下午4:32:22
 */
public class GatewayRequestContext {

	/** sessionId */
	public static final String SESSION_ID_KEY = "sessionId";

	/** session */
	public static final String SESSION_KEY = "session";

	/** properties */
	public static final String PROPERTY_KEY = "properties";

	/** ThreadLocal */
	private static ThreadLocal<Map<Object, Object>> context = new ThreadLocal<Map<Object, Object>>() {
		public Map<Object, Object> initialValue() {
			return new HashMap<Object, Object>();
		}
	};

	/**
	 * 取得指定的key对应的上下文信息，支持泛型
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object key) {
		return (T) context.get().get(key);
	}

	/**
	 * 设置上下文信息
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T put(Object key, Object value) {
		return (T) context.get().put(key, value);
	}

	/**
	 * 取得sessionId
	 * 
	 * @return
	 */
	public static String getSessionId() {
		return get(SESSION_ID_KEY);
	}

	/**
	 * 设置sessionId
	 * 
	 * @param sessionId
	 */
	public static void setSessionId(String sessionId) {
		put(SESSION_ID_KEY, sessionId);
	}

	/**
	 * 根据请求属性名称取得对应的属性值
	 * 
	 * @param propertyName
	 * @return
	 */
	public static <T> T getRequestProperty(String propertyName) {
		return getRequestProperty(propertyName, null);
	}

	/**
	 * 根据请求属性名称取得对应的属性值，当对应的属性值为null时返回defaultValue
	 * 
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRequestProperty(String propertyName, T defaultValue) {
		Map<String, Object> properties = get(PROPERTY_KEY);
		if (properties == null) {
			return defaultValue;
		}
		T value = (T) properties.get(propertyName);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * 设置扩展参数
	 * 
	 * @param extParams
	 */
	public static void setRequestProperties(Map<String, Object> properties) {
		put(PROPERTY_KEY, properties);
	}

	/**
	 * 清空threadlocal
	 */
	public static void clear() {
		context.remove();
	}
}
