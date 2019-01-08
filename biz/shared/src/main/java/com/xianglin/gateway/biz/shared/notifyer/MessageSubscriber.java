/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer;

/**
 * 消息订阅器
 * 
 * @author pengpeng 2016年1月21日上午10:59:00
 */
public interface MessageSubscriber {

	/**
	 * 订阅
	 * 
	 */
	void subscribe();

	/**
	 * 取消订阅
	 * 
	 */
	void unsubscribe();
}
