/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;

/**
 * ServiceResponse转换器
 * 
 * @author pengpeng 2015年4月24日上午11:29:16
 */
public interface ResponseProcessor<I, O> {

	/**
	 * 将服务方法返回结果转换为ServiceResponse对象。
	 * <ul>
	 * <li>内部需要捕获可预见的异常，并根据情况构造合理的ServiceResponse对象返回。</li>
	 * <li>外部会捕获所有不可预见异常，给网关返回“未知错误”响应。</li>
	 * </ul>
	 * 
	 * @param serviceId
	 * @param requestData
	 * @param response
	 * @return
	 */
	ServiceResponse<O> process(String serviceId, Object response);
}
