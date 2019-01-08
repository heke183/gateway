/**
 * 
 */
package com.xianglin.gateway.web.home.exceptionresolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.web.home.util.WebConstants;
import com.xianglin.gateway.web.home.util.WebUtils;

/**
 * 未捕获异常处理器，防御性容错，防止服务端的未捕获未知异常输出到客户端
 * 
 * @author pengpeng 2016年1月21日下午12:10:13
 */
public class UncaughtExceptionResolver implements HandlerExceptionResolver {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionResolver.class);

	/**
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		String requestUri = request.getRequestURI();
		String method = request.getMethod();
		String clientIp = WebUtils.getClientIp(request);
		String deviceId = WebUtils.getHeader(request, WebConstants.HEADER_DEVICE_ID);

		StringBuffer buffer = new StringBuffer();
		buffer.append("requestUri:").append(requestUri).append(",");
		buffer.append("method:").append(method).append(",");
		buffer.append("deviceId:").append(deviceId).append(",");
		buffer.append("clientIp:").append(clientIp).append(",params:{");

		StringBuffer paramBuffer = new StringBuffer();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration != null && enumeration.hasMoreElements()) {
			String paramName = enumeration.nextElement();
			paramBuffer.append(paramName).append(":").append(request.getParameter(paramName)).append(",");
		}

		buffer.append(StringUtils.stripEnd(paramBuffer.toString(), ",")).append("}");

		logger.error("resolve UncaughtException! " + buffer.toString(), ex);

		// WebUtils.writeJsonToResponse(response,
		// JSON.toJSONString(new
		// ServiceResponse<String>(ResponseEnum.GATEWAY_ERROR)));
		WebUtils.writeJsonToResponse(response, JSON.toJSONString(new ServiceResponse<String>(ResultEnum.UnknowError)));
		return null;
	}

}
