/**
 * 
 */
package com.xianglin.gateway.web.home.util;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * web工具类
 * 
 * @author pengpeng
 */
public class WebUtils {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

	/**
	 * 不支持的请求
	 * 
	 * @param request
	 * @param response
	 */
	public static void unsupportRequest(HttpServletRequest request, HttpServletResponse response) {
		String requestUri = request.getRequestURI();
		String queryString = request.getQueryString();
		String method = request.getMethod();
		String deviceId = WebUtils.getHeader(request, WebConstants.HEADER_DEVICE_ID);

		logger.warn("unsupport request! requestUri:{},queryString:{},method:{},diviceId:{}", requestUri, queryString,
				method, deviceId);

		// writeJsonToResponse(response, JSON.toJSONString(new
		// ServiceResponse<String>(ResponseEnum.UNSUPPORT_REQUEST)));
		writeJsonToResponse(response, JSON.toJSONString(new ServiceResponse<String>(ResultEnum.PermissionDeny)));
	}

	/**
	 * 将json字符串写入http响应中
	 * 
	 * @param response
	 * @param json
	 */
	public static void writeJsonToResponse(HttpServletResponse response, String json) {
		json = StringUtils.trimToEmpty(json);
		if (json.length() > 1024) {
			writeBigJsonToResponse(response, json);
			return;
		}
		response.setContentType(WebConstants.MIME_TYPE_JSON);
		response.setCharacterEncoding(WebConstants.CHARSET);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			logger.error("writeJsonToResponse error!", e);
			throw new RuntimeException(e);
		} finally {
			closeQuietly(writer);
		}
	}

	/**
	 * 将超大json字符串写入http响应中
	 * 
	 * @param response
	 * @param json
	 */
	public static void writeBigJsonToResponse(HttpServletResponse response, String json) {
		json = StringUtils.trimToEmpty(json);
		response.setContentType(WebConstants.MIME_TYPE_JSON);
		response.setCharacterEncoding(WebConstants.CHARSET);
		int bufferSize = response.getBufferSize();
		if (bufferSize == 0) {
			bufferSize = 2048;
			response.setBufferSize(bufferSize);
		}
		byte[] buffer = new byte[bufferSize];
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new ByteArrayInputStream(json.getBytes(WebConstants.CHARSET));
			out = response.getOutputStream();
			int length = -1;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			out.flush();
		} catch (IOException e1) {
			closeQuietly(in);
			closeQuietly(out);
		}
	}

	/**
	 * 安静的关闭
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (Throwable e) {
		}
	}

	/**
	 * 从HttpServletRequest中取指定名称的header
	 * 
	 * @param request
	 * @param header
	 * @param defaultValue
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header, String defaultValue) {
		String result = StringUtils.trimToNull(request.getHeader(header));
		if (StringUtils.isEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从HttpServletRequest中取指定名称的header
	 * 
	 * @param request
	 * @param header
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header) {
		return StringUtils.trimToNull(request.getHeader(header));
	}

	/**
	 * 从HttpServletRequest中取指定名称的cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @param defaultValue
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName, String defaultValue) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return defaultValue;
		}
		String result = null;
		for (Cookie cookie : cookies) {
			if (cookie != null && cookie.getName().equals(cookieName)) {
				result = StringUtils.trimToNull(cookie.getValue());
			}
		}
		if (StringUtils.isEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从HttpServletRequest中取指定名称的cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName) {
		return getCookie(request, cookieName, null);
	}

	/**
	 * 从HttpServletRequest中取指定名称的参数
	 * 
	 * @param request
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param, String defaultValue) {
		String result = StringUtils.trimToNull(request.getParameter(param));
		if (StringUtils.isEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从HttpServletRequest中取指定名称的参数
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param) {
		return StringUtils.trimToNull(request.getParameter(param));
	}

	/**
	 * 取得客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String clientIp = getHeader(request, WebConstants.HEADER_CLIENT_IP, null);
		if (StringUtils.isEmpty(clientIp)) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}
}
