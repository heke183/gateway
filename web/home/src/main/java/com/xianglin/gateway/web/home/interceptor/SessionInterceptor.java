/**
 * 
 */
package com.xianglin.gateway.web.home.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.xianglin.fala.session.Session;
import com.xianglin.fala.session.SessionRepository;
import com.xianglin.gateway.common.util.EnvironmentUtils;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;
import com.xianglin.gateway.web.home.util.SessionCookieHelper;
import com.xianglin.gateway.web.home.util.WebConstants;
import com.xianglin.gateway.web.home.util.WebUtils;

/**
 * 拦截器，统一处理session创建，销毁，续期，sessionId的读取，sessionId cookie的写入
 * 
 * @author pengpeng 2016年1月21日下午4:40:00
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

	/** sessionCookieHelper */
	private SessionCookieHelper sessionCookieHelper;

	/** sessionRepository */
	private SessionRepository<Session> sessionRepository;

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/** env */
	private String env;

	/**
	 * 从globalSession(redis)中加载Session到本地
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (EnvironmentUtils.isNotPrdEnv(env)) {
			// 非生产环境为方便测试做的适配，生产环境不会进入该if语句
			response.setHeader("Access-Control-Allow-Origin", "*");
		}
		//Thread.currentThread().setName(UUID.randomUUID().toString());//设置线程名

		boolean useSession = gatewayConfigRepository.get(Constants.SWITCHER_USE_SESSION, false, Boolean.class);
		if (!useSession) {
			logger.debug("skip session interceptor preHandle!");
			return true;
		}

		Session session = getSession(request, sessionCookieHelper.getSessionId(request));
		if (session == null) {
			throw new RuntimeException("can not get Session from request!");
		}

		logger.debug("getSession success,sessionId:{}", session.getId());

		request.setAttribute(WebConstants.ATTRIBUTE_SESSION, session);
		return true;
	}

	/**
	 * 销毁session，或session续期
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		boolean useSession = gatewayConfigRepository.get(Constants.SWITCHER_USE_SESSION, false, Boolean.class);
		if (!useSession) {
			logger.debug("skip session interceptor postHandle!");
			return;
		}

		Session session = (Session) request.getAttribute(WebConstants.ATTRIBUTE_SESSION);
		if (session.isExpired()) {
			sessionRepository.delete(session.getId());
		} else {
			sessionRepository.save(session);
		}
	}

	/**
	 * session不存在，创建新session，并保存到globalSession(redis)中
	 * 
	 * @param request
	 * @param sessionId
	 * @return
	 */
	private Session getSession(HttpServletRequest request, String sessionId) {
		Session session = null;
		if (StringUtils.isEmpty(sessionId) || (session = sessionRepository.getSession(sessionId)) == null) {
			session = sessionRepository.createSession();
			String deviceId = WebUtils.getHeader(request, WebConstants.HEADER_DEVICE_ID);
			session.setAttribute(WebConstants.HEADER_DEVICE_ID, deviceId);
			sessionRepository.save(session);
			logger.debug("createSession success! oldSessionId:{},newSessionId:{},deviceId:{}", sessionId,
					session.getId(), deviceId);
			return session;
		}
		return session;
	}

	/**
	 * @param sessionCookieHelper
	 *            the sessionCookieHelper to set
	 */
	public void setSessionCookieHelper(SessionCookieHelper sessionCookieHelper) {
		this.sessionCookieHelper = sessionCookieHelper;
	}

	/**
	 * @param sessionRepository
	 *            the sessionRepository to set
	 */
	public void setSessionRepository(SessionRepository<Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	/**
	 * @param env
	 *            the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

}
