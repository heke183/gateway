/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import java.lang.reflect.Type;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xianglin.gateway.common.service.spi.exception.ParamException;

/**
 * JSON格式参数处理器
 * 
 * @author pengpeng 2015年11月13日下午9:54:13
 */
public class JSONParamProcessor extends BaseParamProcessor<String> {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(JSONParamProcessor.class);

	/**
	 * @see com.xianglin.gateway.common.service.spi.ParamProcessor#process(java.lang.String,
	 *      java.lang.Object, java.lang.reflect.Type[])
	 */
	@Override
	public Object[] processParam(String serviceId, String requestData, Type[] types) throws ParamException {
		logger.debug("process json param start, serviceId:{},requestData:{},types:{}", serviceId, requestData,
				ArrayUtils.toString(types, null));
		Object[] result = JSON.parseArray(requestData, types).toArray();
		logger.debug("process json param end, result:{}", ArrayUtils.toString(result, null));
		return result;
	}

}
