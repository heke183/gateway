/**
 * 
 */
package com.xianglin.gateway.web.home.util;

import com.xianglin.gateway.core.model.Constants;

/**
 * web层常量定义
 * 
 * @author pengpeng
 *
 */
public class WebConstants {

	/** http响应MIME类型：json */
	public static final String MIME_TYPE_JSON = "application/json";

	/** CHARSET */
	public static final String CHARSET = "utf-8";

	/** http请求header名称：客户端ip */
	public static final String HEADER_CLIENT_IP = "X-Forwarded-For";

	/** http请求header名称：If-None-Match */
	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";

	/** http响应header名称：ETag */
	public static final String HEADER_ETAG = "ETag";

	/** http请求header名称：设备唯一标识 */
	public static final String HEADER_DEVICE_ID = Constants.DEVICE_ID;

	/** http请求参数名称：operationType */
	public static final String PARAM_OPERATION = "operationType";

	/** http请求参数名称：requestData */
	public static final String PARAM_REQUEST_DATA = "requestData";

	/** http请求属性名称：session */
	public static final String ATTRIBUTE_SESSION = "session";

}
