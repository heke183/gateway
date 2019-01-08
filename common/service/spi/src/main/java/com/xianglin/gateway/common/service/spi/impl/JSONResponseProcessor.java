/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * JSON响应处理器
 * 
 * @author pengpeng 2015年11月13日下午10:16:00
 */
public class JSONResponseProcessor extends BaseResponseProcessor<String, String> {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(JSONResponseProcessor.class);

	/**
	 * @see com.xianglin.gateway.common.service.spi.impl.BaseResponseProcessor#processResponse(java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public String processResponse(String serviceId, Object response) {
		logger.debug("process response start, serviceId:{}, types:{}", serviceId, response);
		String result = null;
		if (response instanceof String) {
			result = response.toString();
		} else {
			result = JSON.toJSONString(response);
		}
		logger.debug("process response end, result:{}", result);
		return result;
	}

}
