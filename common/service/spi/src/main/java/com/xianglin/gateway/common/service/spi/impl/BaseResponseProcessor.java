/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.service.spi.ResponseProcessor;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 默认响应处理器
 * 
 * @author pengpeng 2015年5月3日下午2:37:31
 */
public abstract class BaseResponseProcessor<I, O> implements ResponseProcessor<I, O> {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(BaseResponseProcessor.class);

	/**
	 * @see com.xianglin.gateway.common.service.spi.ResponseProcessor#process(java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ServiceResponse<O> process(String serviceId, Object response) {
		ServiceResponse<O> result = null;
		try {
			if (response instanceof ServiceResponse<?>) {
				result = (ServiceResponse<O>) response;
			} else {
				result = new ServiceResponse<O>(processResponse(serviceId, response));
			}
		} catch (Throwable e) {
			logger.error("process response error!", e);
			// result = new ServiceResponse<O>(ResponseEnum.SERVICE_ERROR);
			response = new ServiceResponse<O>(ResultEnum.BizException);
		}

		return result;
	}

	/**
	 * 实际的类型转换
	 * 
	 * @param serviceId
	 * @param requestData
	 * @param response
	 * @return
	 */
	public abstract O processResponse(String serviceId, Object response);

}
