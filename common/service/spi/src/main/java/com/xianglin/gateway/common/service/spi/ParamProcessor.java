/**
 * 
 */
package com.xianglin.gateway.common.service.spi;

import java.lang.reflect.Type;

import com.xianglin.gateway.common.service.spi.exception.ParamException;

/**
 * 服务方法参数转换器
 * 
 * @author pengpeng 2015年4月24日上午11:26:37
 */
public interface ParamProcessor<T> {

	/**
	 * 将String类型的参数转换为服务方法参数
	 * <ol>
	 * <li>内部遇到参数转换异常无法继续处理时，抛出异常以中断流程，由ExceptionResolver统一处理。</li>
	 * <li>抛出的异常需要包含足够的信息，以便于ExceptionResolver可以根据这些信息返回恰当的ServiceResponse。</li>
	 * <li>如有必要可使用自定义异常类型。</li>
	 * </ol>
	 * 
	 * @param serviceId
	 *            自定义参数转换器可以根据该参数选择对应的转换策略
	 * @param requestData
	 * @param types
	 * @return
	 */
	Object[] process(String serviceId, T requestData, Type[] types) throws ParamException;

}
