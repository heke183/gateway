/**
 * 
 */
package com.xianglin.gateway.web.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.xianglin.fala.session.Session;
import com.xianglin.gateway.biz.shared.GatewayServiceProcessor;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.common.util.EnvironmentUtils;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.model.SingleResponseBody;
import com.xianglin.gateway.core.service.repository.GatewayServiceConfigRepository;
import com.xianglin.gateway.web.home.util.GatewayRequestFactory;
import com.xianglin.gateway.web.home.util.SessionCookieHelper;
import com.xianglin.gateway.web.home.util.WebConstants;
import com.xianglin.gateway.web.home.util.WebUtils;

import java.util.UUID;

/**
 * 网关http请求入口
 * 
 * @author pengpeng 2016年1月21日下午4:24:12
 */
@Controller("apiController")
public class ApiController {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

	/** gatewayServiceProcessor */
	private GatewayServiceProcessor gatewayServiceProcessor;

	/** gatewayRequestFactory */
	private GatewayRequestFactory gatewayRequestFactory;

	/** sessionCookieHelper */
	private SessionCookieHelper sessionCookieHelper;

	/** gatewayServiceConfigRepository */
	private GatewayServiceConfigRepository gatewayServiceConfigRepository;

	/** env */
	private String env;

	/**
	 * json格式的请求生产环境只能使用post方法
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/api.json", "/ggw/mgw.htm" }, method = { RequestMethod.POST })
	public void post(HttpServletRequest request, HttpServletResponse response) {
		GatewayRequest<String> gatewayRequest = gatewayRequestFactory.createJsonGatewayRequest(request);
		logger.debug("gateway start, gatewayRequest:{}", gatewayRequest);

		GatewayResponse gatewayResponse = new GatewayResponse();
		try {
			gatewayServiceProcessor.process(gatewayRequest, gatewayResponse);
		} catch (Throwable e) {
			logger.error("gatewayServiceProcessor error!", e);
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.GATEWAY_ERROR));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.UnknowError));
		}

		ServiceResponse<?> serviceResponse = gatewayResponse.getServiceResponse();

		Session session = gatewayRequest.getSession();
		if(serviceResponse.getCode() == ResultEnum.PermissionDeny.getCode()){//出现拒绝访问
			session.setMaxInactiveIntervalInSeconds(-1);//将session过期掉
		}
		request.setAttribute(WebConstants.ATTRIBUTE_SESSION, session);
		// 在返回结果前设置cookie
		sessionCookieHelper.setSessionCookie(response, session);

		String json = JSON.toJSONString(SingleResponseBody.valueOf(serviceResponse));

		// 超大响应内容（可能包含文件）暂不打日志
		if (json.length() <= 1024) {
			logger.debug("gateway end, serviceResponse:{}", json);
		}

		if (!serviceResponse.isSuccess()) {
			WebUtils.writeJsonToResponse(response, json);
			return;
		}
		boolean useETag = gatewayServiceConfigRepository.get(gatewayRequest.getOperation()).isUseETag();
		if (!useETag) {
			WebUtils.writeJsonToResponse(response, json);
			return;
		}
		String ETag = '"' + DigestUtils.md5Hex(json) + '"';
		response.setHeader(WebConstants.HEADER_ETAG, ETag);

		String previousETag = request.getHeader(WebConstants.HEADER_IF_NONE_MATCH);
		if (StringUtils.equals(ETag, previousETag)) {
			response.setContentLength(0);
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			logger.info("ETag match! return 304(not modified)");
			return;
		}

		logger.info("ETag do not match! ETag:{},previousETag:{}", ETag, previousETag);
		WebUtils.writeJsonToResponse(response, json);
	}

	/**
	 * 不支持的请求
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/api.json", "/api.prot", "/ggw/mgw.htm" }, method = { RequestMethod.DELETE,
			RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE })
	public void unsupport(HttpServletRequest request, HttpServletResponse response) {
		WebUtils.unsupportRequest(request, response);
	}

	/**
	 * json格式的请求，get方法特殊处理，方便非生产环境测试
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/api.json", "/ggw/mgw.htm" }, method = { RequestMethod.GET })
	public void jsonGet(HttpServletRequest request, HttpServletResponse response) {
		if (EnvironmentUtils.isNotPrdEnv(env)) {
			// 非生产环境为方便测试做的适配，生产环境不会进入该if语句
			post(request, response);
			return;
		}

		unsupport(request, response);
	}

	/**
	 * protobuf格式的请求，get方法不支持
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/api.prot" }, method = { RequestMethod.GET })
	public void protobufGet(HttpServletRequest request, HttpServletResponse response) {
		unsupport(request, response);
	}

	/**
	 * @param gatewayServiceProcessor
	 *            the gatewayServiceProcessor to set
	 */
	public void setGatewayServiceProcessor(GatewayServiceProcessor gatewayServiceProcessor) {
		this.gatewayServiceProcessor = gatewayServiceProcessor;
	}

	/**
	 * @param gatewayRequestFactory
	 *            the gatewayRequestFactory to set
	 */
	public void setGatewayRequestFactory(GatewayRequestFactory gatewayRequestFactory) {
		this.gatewayRequestFactory = gatewayRequestFactory;
	}

	/**
	 * @param sessionCookieHelper
	 *            the sessionCookieHelper to set
	 */
	public void setSessionCookieHelper(SessionCookieHelper sessionCookieHelper) {
		this.sessionCookieHelper = sessionCookieHelper;
	}

	/**
	 * @param gatewayServiceConfigRepository
	 *            the gatewayServiceConfigRepository to set
	 */
	public void setGatewayServiceConfigRepository(GatewayServiceConfigRepository gatewayServiceConfigRepository) {
		this.gatewayServiceConfigRepository = gatewayServiceConfigRepository;
	}

	/**
	 * @param env
	 *            the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}

}
