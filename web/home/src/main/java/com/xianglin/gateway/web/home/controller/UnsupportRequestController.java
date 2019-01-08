/**
 * 
 */
package com.xianglin.gateway.web.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xianglin.gateway.web.home.util.WebUtils;

/**
 * “不支持的请求” Controller，对于不支持的请求，返回合理的响应，防止404
 * 
 * @author pengpeng 2016年1月21日下午2:45:05
 */
@Controller
@RequestMapping(value = { "", "/*" })
public class UnsupportRequestController {

	/**
	 * 不支持的请求处理
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.HEAD,
			RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE })
	public void unsupport(HttpServletRequest request, HttpServletResponse response) {
		WebUtils.unsupportRequest(request, response);
	}
}
