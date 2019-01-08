/**
 * 
 */
package com.xianglin.gateway.common.service.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xianglin.gateway.common.service.spi.model.enums.Protocol;

/**
 * 该注解加在服务实现类上，用于注明实现的网关服务接口，自动扫描网关服务时用。
 * 
 * @author pengpeng 2015年9月8日下午5:10:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceInterface {

	/** 服务接口 */
	Class<?> value() default Void.class;

	/** 服务协议 */
	Protocol protocol() default Protocol.dubbo;

}
