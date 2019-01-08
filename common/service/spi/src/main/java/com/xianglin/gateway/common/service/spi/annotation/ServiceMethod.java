/**
 * 
 */
package com.xianglin.gateway.common.service.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xianglin.gateway.common.service.spi.model.enums.RpcType;

/**
 * 网关服务方法配置，该注解加在服务接口的方法上，自动扫描网关服务时用。
 * 
 * @author pengpeng 2015年9月8日下午3:55:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceMethod {

	/** 服务方法名称 */
	RpcType rpcType() default RpcType.service;

	/** 服务方法别名 */
	String alias() default "";

	/** 是否只有登录后才能调用 */
	boolean needLogin() default true;

	/** 调用服务前是否需要更换session */
	boolean changeSession() default false;

	/** 调用服务后是否需要删除session */
	boolean deleteSession() default false;

	/** 是否启用ETag做响应缓存 */
	boolean useETag() default false;

	/** 服务调用超时时间，默认3秒钟，如果还不够，一般情况下是服务实现有问题，请优先考虑优化性能！ */
	// TODO: 2017/10/16 超时时间由3000改为20000 
	int timeout() default 30000;

	/** 描述/备注信息，用来说明服务用途 */
	String description();
}
