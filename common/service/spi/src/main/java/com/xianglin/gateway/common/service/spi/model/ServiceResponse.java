/**
 * 
 */
package com.xianglin.gateway.common.service.spi.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 服务响应信息
 * 
 * @author pengpeng
 */
public class ServiceResponse<T> implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -480446657057416066L;

	/** 服务结果码，用户返回给客户端程序，以决定下一步操作。默认为success */
	// private int code = ResponseEnum.SUCCESS.getCode();
	private int code = ResultEnum.ResultSuccess.getCode();

	/** 结果描述，给调用方系统看的描述信息 */
	private String memo;

	/** 返回给调用方，需要显示给用户的的友好提示信息，请谨慎定义 */
	private String tips;

	/** 服务结果 */
	private T result;

	/**
	 * 默认构造函数
	 */
	public ServiceResponse() {

	}

	/**
	 * @param result
	 */
	public ServiceResponse(T result) {
		this.result = result;
	}

	// /**
	// * @param responseEnum
	// */
	// public ServiceResponse(ResponseEnum responseEnum) {
	// this.code = responseEnum.getCode();
	// this.memo = responseEnum.getMemo();
	// this.tip = responseEnum.getTip();
	// }

	/**
	 * @param resultEnum
	 */
	public ServiceResponse(ResultEnum resultEnum) {
		this.code = resultEnum.getCode();
		this.memo = resultEnum.getMemo();
		this.tips = resultEnum.getTips();
	}

	/**
	 * 构造方法
	 * 
	 * @param code
	 * @param message
	 * @param result
	 */
	public ServiceResponse(int code, String memo, String tips) {
		this.code = code;
		this.memo = memo;
		this.tips = tips;
	}

	public boolean isSuccess() {
		// return ResponseEnum.SUCCESS.getCode() == code;
		return ResultEnum.ResultSuccess.getCode() == code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * @param tips
	 *            the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	/**
	 * @return the result
	 */
	public T getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(T result) {
		this.result = result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
