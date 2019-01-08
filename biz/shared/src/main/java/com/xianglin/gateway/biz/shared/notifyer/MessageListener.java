/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer;

/**
 * 通知消息监听器
 * 
 * @author pengpeng 2016年1月21日上午10:56:13
 */
public interface MessageListener {

	/**
	 * 执行通知消息处理逻辑
	 * 
	 */
	void onMessage(String notifyMessage);
}
