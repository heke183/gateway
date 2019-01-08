/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;

/**
 * 异常处理器
 * 
 * @author pengpeng 2015年4月29日上午11:44:10
 */
public interface ExceptionProcessor<I, O> {

	/**
	 * 处理异常。
	 * <ul>
	 * <li>服务实现内部需要捕获可预见的异常，并根据情况构造合理的ServiceResponse对象返回。</li>
	 * <li>外部会捕获所有不可预见异常，给网关返回“未知错误”响应。</li>
	 * </ul>
	 * 
	 * @param serviceId
	 * @param requestData
	 * @param exception
	 * @return
	 */
	ServiceResponse<O> process(String serviceId, Throwable exception);
}
