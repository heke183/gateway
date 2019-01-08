/**
 * 
 */
package com.xianglin.gateway.common.service.facade.model;

import java.io.Serializable;

/**
 * 通用服务响应结果
 * 
 * @author pengpeng 2016年2月18日下午4:04:56
 */
public class Response<T> implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 5670390880829918366L;

	/** 成功的结果码 */
	private static final int SUCCESS_CODE = 1000;

	/** 响应结果码 */
	private int code;

	/** 结果描述，给调用方系统看的描述信息 */
	private String memo;

	/** 返回给调用方，需要显示给用户的的友好提示信息，请谨慎定义 */
	private String tips;

	/** 服务结果 */
	private T result = null;

	/**创建一个默认成功的返回
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> Response<T> newInstence(T t){
		return new Response<>(t);
	}

	/** 隐藏构造方法
	 * @param t
	 */
	public Response(T t){
		this.code = SUCCESS_CODE;
		this.result = t;
	}

	/** 设置通用返回信息
	 * @param resultEnum
	 */
	public void setResultEnum(ResultEnum resultEnum){
		setResonpse(resultEnum.getCode(),resultEnum.getMemo(),resultEnum.getTips());
	}

	/** 设置返回码
	 * @param code
	 * @param memo
	 * @param tip
	 */
	public void setResonpse(int code,String memo,String tip){
		this.code = code;
		this.memo = memo;
		this.tips = tip;
	}

	/**
	 * 操作是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return code == SUCCESS_CODE;
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
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "Response [code=" + code + ", memo=" + memo + ", tips=" + tips + ", result=" + result + "]";
	}
}
