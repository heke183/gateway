/**
 * 
 */
package com.xianglin.gateway.biz.shared.notifyer.impl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.xianglin.gateway.biz.shared.notifyer.MessageListener;
import com.xianglin.gateway.biz.shared.notifyer.MessageSubscriber;

/**
 * Zookeeper通知消息监听器
 * 
 * @author pengpeng 2015年5月20日
 */
public class ZookeeperMessageSubscriber implements MessageSubscriber, InitializingBean, DisposableBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperMessageSubscriber.class);

	/** threadpool */
	private ExecutorService threadpool = Executors.newCachedThreadPool();

	/** curatorFramework */
	private CuratorFramework curatorFramework;

	/** nodeCache */
	private NodeCache nodeCache;

	/** 用于通知的zookeeper服务器地址 */
	private String notifyServerAddress;

	/** 用于通知的zookeeper节点路径 */
	private String notifyPath;

	/** 用于通知的zookeeper命名空间 */
	private String notifyNamespase;

	/** listenerMap */
	private Map<String, MessageListener> listenerMap;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		subscribe();
	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		unsubscribe();
	}

	/**
	 * @see com.xianglin.gateway.biz.shared.notifyer.MessageSubscriber#subscribe()
	 */
	@Override
	public void subscribe() {
		try {
			Builder builder = CuratorFrameworkFactory.builder().connectString(notifyServerAddress)
					.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000)
					.namespace(notifyNamespase);
			curatorFramework = builder.build();
			curatorFramework.start();

			if (curatorFramework.checkExists().forPath(notifyPath) == null) {
				CreateBuilder createBuilder = curatorFramework.create();
				createBuilder.withMode(CreateMode.EPHEMERAL);
				createBuilder.creatingParentsIfNeeded().forPath(notifyPath);
			}

			nodeCache = new NodeCache(curatorFramework, notifyPath, false);
			nodeCache.start();
			nodeCache.getListenable().addListener(new NodeCacheListener() {

				private boolean isFirst = true;

				public void nodeChanged() throws Exception {
					if (nodeCache.getCurrentData() == null) {
						return;
					}
					String notifyMessage = new String(nodeCache.getCurrentData().getData());

					if (isFirst) {
						logger.info("zookeeper message arrived for the first time when system startup, ignore it!");
						isFirst = false;
					}

					process(notifyMessage);
				}
			});
		} catch (Exception e) {
			logger.error("zookeeper subscribe error!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see com.xianglin.gateway.biz.shared.notifyer.MessageSubscriber#unsubscribe()
	 */
	@Override
	public void unsubscribe() {
		try {
			if (nodeCache != null) {
				nodeCache.close();
			}
			if (curatorFramework != null) {
				curatorFramework.close();
			}
		} catch (Throwable e) {
			logger.error("unsubscribe error!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 处理消息
	 * 
	 * @param notifyMessage
	 */
	private void process(final String notifyMessage) {
		logger.info("notifyMessage recived:{}", notifyMessage);
		final MessageListener listener = listenerMap.get(notifyMessage);
		if (listener == null) {
			logger.warn("unsupport nofigyMessage:{}", notifyMessage);
			return;
		}
		threadpool.execute(new Runnable() {
			@Override
			public void run() {
				listener.onMessage(notifyMessage);
			}
		});

	}

	/**
	 * @param notifyServerAddress
	 *            the notifyServerAddress to set
	 */
	public void setNotifyServerAddress(String notifyServerAddress) {
		this.notifyServerAddress = notifyServerAddress;
	}

	/**
	 * @param notifyPath
	 *            the notifyPath to set
	 */
	public void setNotifyPath(String notifyPath) {
		this.notifyPath = notifyPath;
	}

	/**
	 * @param notifyNamespase
	 *            the notifyNamespase to set
	 */
	public void setNotifyNamespase(String notifyNamespase) {
		this.notifyNamespase = notifyNamespase;
	}

	/**
	 * @param listenerMap
	 *            the listenerMap to set
	 */
	public void setListenerMap(Map<String, MessageListener> listenerMap) {
		this.listenerMap = listenerMap;
	}

}
