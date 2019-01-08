/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import java.lang.reflect.Type;

import com.xianglin.gateway.common.service.spi.ParamProcessor;
import com.xianglin.gateway.common.service.spi.exception.ParamException;

/**
 * 参数处理器基类
 * 
 * @author pengpeng 2015年4月29日下午3:20:41
 */
public abstract class BaseParamProcessor<T> implements ParamProcessor<T> {

	/**
	 * @see com.xianglin.gateway.common.service.spi.ParamProcessor#process(java.lang.String,
	 *      java.lang.String, java.lang.reflect.Type[])
	 */
	@Override
	public Object[] process(String serviceId, T requestData, Type[] types) {
		Object[] params = null;
		try {
			params = processParam(serviceId, requestData, types);
		} catch (Throwable e) {
			throw new ParamException("param process error!", e);
		}
		// 禁止返回null
		if (params == null) {
			throw new ParamException("param is null!");
		}

		// 参数个数校验
		if (params == null || params.length != types.length) {
			String message = "param count error! expectedCount:" + types.length + ", realCount:" + params.length;
			throw new ParamException(message);
		}
		return params;
	}

	/**
	 * 参数解析
	 * 
	 * @param serviceId
	 * @param requestData
	 * @param types
	 * @return
	 */
	public abstract Object[] processParam(String serviceId, T requestData, Type[] types);

}
