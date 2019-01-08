/**
 * 
 */
package com.xianglin.gateway.web.home.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.util.EnvironmentUtils;
import com.xianglin.gateway.core.model.GatewayRequest;

/**
 * GatewayRequest工厂
 * 
 * @author pengpeng 2016年1月21日下午3:03:57
 */
public class GatewayRequestFactory {

	/** env */
	private String env;

	/**
	 * 创建Json格式数据的GatewayRequest
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	public GatewayRequest<String> createJsonGatewayRequest(HttpServletRequest httpServletRequest) {
		GatewayRequest<String> gatewayRequest = new GatewayRequest<String>();
		setSession(httpServletRequest, gatewayRequest);
		setDeviceId(httpServletRequest, gatewayRequest);
		setOperation(httpServletRequest, gatewayRequest);
		setJsonRequestData(httpServletRequest, gatewayRequest);
		setParams(httpServletRequest, gatewayRequest);
		return gatewayRequest;
	}

	/**
	 * 设置session信息
	 * 
	 * @param httpServletRequest
	 * @param gatewayRequest
	 */
	private void setSession(HttpServletRequest httpServletRequest, GatewayRequest<?> gatewayRequest) {
		Session session = (Session) httpServletRequest.getAttribute(WebConstants.ATTRIBUTE_SESSION);
		gatewayRequest.setSession(session);
	}

	/**
	 * 设置deviceId
	 * 
	 * @param httpServletRequest
	 * @param gatewayRequest
	 */
	private void setDeviceId(HttpServletRequest httpServletRequest, GatewayRequest<?> gatewayRequest) {
		String deviceId = WebUtils.getHeader(httpServletRequest, WebConstants.HEADER_DEVICE_ID);
		// 非生产环境为方便测试做的适配，生产环境不会执行以下逻辑
		if (StringUtils.isEmpty(deviceId) && EnvironmentUtils.isNotPrdEnv(env)) {
			deviceId = WebUtils.getParam(httpServletRequest, WebConstants.HEADER_DEVICE_ID, null);
		}
		gatewayRequest.setDeviceId(deviceId);
	}

	/**
	 * 设置operation
	 * 
	 * @param httpServletRequest
	 * @param gatewayRequest
	 */
	private void setOperation(HttpServletRequest httpServletRequest, GatewayRequest<?> gatewayRequest) {
		gatewayRequest.setOperation(WebUtils.getParam(httpServletRequest, WebConstants.PARAM_OPERATION));
	}

	/**
	 * 设置json格式的请求数据
	 * 
	 * @param httpServletRequest
	 * @param gatewayRequest
	 */
	private void setJsonRequestData(HttpServletRequest httpServletRequest, GatewayRequest<String> gatewayRequest) {
		gatewayRequest.setRequestData(WebUtils.getParam(httpServletRequest, WebConstants.PARAM_REQUEST_DATA));
	}

	/**
	 * 设置请求参数
	 * 
	 * @param httpServletRequest
	 * @param gatewayRequest
	 */
	private static void setParams(HttpServletRequest httpServletRequest, GatewayRequest<?> gatewayRequest) {
		Enumeration<String> paramNames = httpServletRequest.getParameterNames();
		if (paramNames == null) {
			return;
		}
		String key = null;
		String value = null;
		for (; paramNames.hasMoreElements();) {
			key = StringUtils.trimToNull(paramNames.nextElement());
			value = StringUtils.trimToEmpty(httpServletRequest.getParameter(key));
			if (StringUtils.isEmpty(key)) {
				continue;
			}
			gatewayRequest.setParam(key, value);
		}
		gatewayRequest.setParam("remoteAddr",httpServletRequest.getRemoteAddr());//保存用户ip地址
		gatewayRequest.setParam("remoteHost",httpServletRequest.getRemoteHost());//保存用户ip地址
	}

	/**
	 * @param env
	 *            the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}

}
