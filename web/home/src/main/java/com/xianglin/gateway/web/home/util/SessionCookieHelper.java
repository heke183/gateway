package com.xianglin.gateway.web.home.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;

/**
 * sessionId读取，cookie写入助手
 * 
 * @author pengpeng 2016年1月21日下午4:51:25
 */
public class SessionCookieHelper {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SessionCookieHelper.class);

	/** sessionId cookie名称 */
	private String sessionCookieName;

	/** sessionCookieDomain */
	private String sessionCookieDomain;

	/** sessionCookiePath */
	private String sessionCookiePath = "/";

	/** sessionCookieHttpOnly */
	private boolean sessionCookieHttpOnly = true;

	/** sessionCookieSecure */
	private boolean sessionCookieSecure = true;

	/** sessionCookieMaxAge */
	private int sessionCookieMaxAge = -1;

	/**
	 * 从http请求中取得sessionId
	 * 
	 * @param request
	 * @return
	 */
	public String getSessionId(HttpServletRequest request) {
		return WebUtils.getCookie(request, sessionCookieName, null);
	}

	/**
	 * 设置sessionId cookie到http响应中
	 * 
	 * @param response
	 * @param session
	 */
	public void setSessionCookie(HttpServletResponse response, Session session) {
		if (session == null) {
			return;
		}
		String sessionId = session.getId();

		logger.debug("response session cookie:{}", sessionId);

		Cookie cookie = new Cookie(sessionCookieName, sessionId);
		if (StringUtils.isNotEmpty(sessionCookieDomain)) {
			cookie.setDomain(sessionCookieDomain);
		}
		if (StringUtils.isNotEmpty(sessionCookiePath)) {
			cookie.setPath(sessionCookiePath);
		} else {
			cookie.setPath("/");
		}
		if (sessionCookieHttpOnly) {
			cookie.setHttpOnly(true);
		} else {
			cookie.setHttpOnly(false);
		}
		if (sessionCookieSecure) {
			cookie.setSecure(true);
		} else {
			cookie.setSecure(false);
		}

		if (session.isExpired()) {
			// 如果session已过期，则设置cookie过期时间为0，使浏览器中cookie立即失效
			cookie.setMaxAge(0);
		} else {
			// 重设cookie过期时间
			cookie.setMaxAge(sessionCookieMaxAge);
		}

		response.addCookie(cookie);
	}

	/**
	 * @return the sessionCookieName
	 */
	public String getSessionCookieName() {
		return sessionCookieName;
	}

	/**
	 * @param sessionCookieName
	 *            the sessionCookieName to set
	 */
	public void setSessionCookieName(String sessionCookieName) {
		this.sessionCookieName = sessionCookieName;
	}

	/**
	 * @return the sessionCookieDomain
	 */
	public String getSessionCookieDomain() {
		return sessionCookieDomain;
	}

	/**
	 * @param sessionCookieDomain
	 *            the sessionCookieDomain to set
	 */
	public void setSessionCookieDomain(String sessionCookieDomain) {
		this.sessionCookieDomain = sessionCookieDomain;
	}

	/**
	 * @return the sessionCookiePath
	 */
	public String getSessionCookiePath() {
		return sessionCookiePath;
	}

	/**
	 * @param sessionCookiePath
	 *            the sessionCookiePath to set
	 */
	public void setSessionCookiePath(String sessionCookiePath) {
		this.sessionCookiePath = sessionCookiePath;
	}

	/**
	 * @return the sessionCookieHttpOnly
	 */
	public boolean isSessionCookieHttpOnly() {
		return sessionCookieHttpOnly;
	}

	/**
	 * @param sessionCookieHttpOnly
	 *            the sessionCookieHttpOnly to set
	 */
	public void setSessionCookieHttpOnly(boolean sessionCookieHttpOnly) {
		this.sessionCookieHttpOnly = sessionCookieHttpOnly;
	}

	/**
	 * @return the sessionCookieSecure
	 */
	public boolean isSessionCookieSecure() {
		return sessionCookieSecure;
	}

	/**
	 * @param sessionCookieSecure
	 *            the sessionCookieSecure to set
	 */
	public void setSessionCookieSecure(boolean sessionCookieSecure) {
		this.sessionCookieSecure = sessionCookieSecure;
	}

	/**
	 * @return the sessionCookieMaxAge
	 */
	public int getSessionCookieMaxAge() {
		return sessionCookieMaxAge;
	}

	/**
	 * @param sessionCookieMaxAge
	 *            the sessionCookieMaxAge to set
	 */
	public void setSessionCookieMaxAge(int sessionCookieMaxAge) {
		this.sessionCookieMaxAge = sessionCookieMaxAge;
	}
}
