package com.xianglin.gateway.core.service.signature;

/**
 * 签名管理器
 * 
 * @author pengpeng 2015年5月7日下午9:13:12
 */
public interface SignatureService {

	/**
	 * 计算签名
	 * 
	 * @param toBeSigned
	 * @return
	 * @throws Exception
	 */
	String sign(String toBeSigned) throws Exception;

	/**
	 * 验证签名
	 * 
	 * @param toBeSigned
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	boolean verify(String toBeSigned, String sign) throws Exception;

}