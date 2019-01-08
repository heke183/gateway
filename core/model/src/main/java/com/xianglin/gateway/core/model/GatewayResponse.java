/**

 * 
 */
package com.xianglin.gateway.core.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 网关响应
 * 
 * @author pengpeng 2015年4月27日下午2:15:04
 */
public class GatewayResponse {

	/** serviceResponse */
	private ServiceResponse<?> serviceResponse = new ServiceResponse<String>(ResultEnum.InvokeExceedLimit);// 暂时用这个代替

	/**
	 * @return the serviceResponse
	 */
	public ServiceResponse<?> getServiceResponse() {
		return serviceResponse;
	}

	/**
	 * @param serviceResponse
	 *            the serviceResponse to set
	 */
	public void setServiceResponse(ServiceResponse<?> serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
