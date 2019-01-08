/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import com.xianglin.gateway.common.service.spi.ExceptionProcessor;
import com.xianglin.gateway.common.service.spi.exception.ParamException;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 默认异常处理器
 * 
 * @author pengpeng 2015年5月3日下午2:44:30
 */
public class DefaultExceptionProcessor<I, O> implements ExceptionProcessor<I, O> {

	/**
	 * @see com.xianglin.gateway.common.service.spi.ExceptionProcessor#process(java.lang.String,
	 *      java.lang.String, java.lang.Throwable)
	 */
	@Override
	public ServiceResponse<O> process(String serviceId, Throwable exception) {
		// ServiceResponse<O> result = new
		// ServiceResponse<O>(ResponseEnum.SERVICE_ERROR);
		ServiceResponse<O> result = new ServiceResponse<O>(ResultEnum.BizException);
		if (exception instanceof ParamException) {
			// result = new ServiceResponse<O>(ResponseEnum.PARAM_ERROR);
			result = new ServiceResponse<O>(ResultEnum.IllegalArgument);
		}
		return result;
	}

}
