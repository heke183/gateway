/**
 * 
 */
package com.xianglin.gateway.common.service.spi.exception;

/**
 * 服务方法未找到异常
 * 
 * @author pengpeng
 */
public class ServiceMethodNotFoundException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 4895337415739984361L;

	/**
	 * 构造方法
	 * 
	 * @param serviceId
	 */
	public ServiceMethodNotFoundException(String serviceId) {
		super("service method not found! serviceMethod:" + serviceId);
	}

}
