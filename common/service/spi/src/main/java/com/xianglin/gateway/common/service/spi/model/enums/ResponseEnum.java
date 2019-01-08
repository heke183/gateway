/**
 * 
 */
package com.xianglin.gateway.common.service.spi.model.enums;

/**
 * 响应枚举
 * 
 * @author pengpeng 2015年11月13日下午9:21:03
 */
public enum ResponseEnum {

//	/** 调用成功 */
//	SUCCESS(2000, null, null),
//
//	/** 需要登录 */
//	NEED_LOGIN(3000, "need login!", "请您登录!"),
//
//	/** 请求参数错误，一般错误在请求发起方 */
//	PARAM_ERROR(4000, "illegal param!", "抱歉，暂时无法操作，请稍后再试！"),
//
//	/** 不支持的请求，一般错误在请求发起方，比如请求url错误，或者operationType错误 */
//	UNSUPPORT_REQUEST(4001, "unsupport request!", "抱歉，暂时无法操作，请稍后再试！"),
//
//	/** 业务服务错误，一般错误在业务服务提供方 */
//	SERVICE_ERROR(5000, "service error!", "抱歉，暂时无法操作，请稍后再试！"),
//
//	/** RPC错误，一般错误原因为网络调用层面的异常，比如dubbo、网络、超时等 */
//	RPC_ERROR(5001, "rpc error!", "抱歉，暂时无法操作，请稍后再试！"),
//
//	/** 网关错误，一般错误原因在网关 */
//	GATEWAY_ERROR(5002, "gateway error!", "抱歉，暂时无法操作，请稍后再试！"),

	;

	/** code */
	private int code;

	/** memo */
	private String memo;

	/** tip */
	private String tip;

	/**
	 * 构造方法
	 * 
	 * @param code
	 * @param memo
	 * @param tip
	 */
	private ResponseEnum(int code, String memo, String tip) {
		this.code = code;
		this.memo = memo;
		this.tip = tip;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the tip
	 */
	public String getTip() {
		return tip;
	}
}
