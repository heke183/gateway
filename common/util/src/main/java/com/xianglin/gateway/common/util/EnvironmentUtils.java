/**
 * 
 */
package com.xianglin.gateway.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 运行环境工具类
 * 
 * @author pengpeng 2016年1月21日下午3:16:46
 */
public class EnvironmentUtils {

	/** 生产环境 */
	public static final String ENV_PRD = "prd";

	/**
	 * 是否是生产环境
	 * 
	 * @param env
	 * @return
	 */
	public static boolean isPrdEnv(String env) {
		return StringUtils.equalsIgnoreCase(env, ENV_PRD);
	}

	/**
	 * 是否不是生产环境
	 * 
	 * @param env
	 * @return
	 */
	public static boolean isNotPrdEnv(String env) {
		return !isPrdEnv(env);
	}
}
