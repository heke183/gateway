/**
 * 
 */
package com.xianglin.gateway.common.service.spi.exception;

/**
 * 通用异常基类
 * 
 * @author pengpeng 2015年4月29日下午3:25:58
 */
public class ParamException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 4142698502316986594L;

	/**
	 * 构造方法
	 * 
	 * @param message
	 * @param cause
	 */
	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造方法
	 * 
	 * @param message
	 */
	public ParamException(String message) {
		super(message);
	}

}
