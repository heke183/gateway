/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.xianglin.gateway.common.service.spi.ServiceMethodInfoRepository;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceMethodInfo;
import com.xianglin.gateway.common.service.spi.util.ClassScanUtil;

/**
 * 网关服务方法仓库实现类，带有自动扫描功能
 * 
 * @author pengpeng 2015年9月6日下午3:47:13
 */
public class ServiceMethodInfoRepositoryImpl
		implements ServiceMethodInfoRepository, ApplicationContextAware, InitializingBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ServiceMethodInfoRepositoryImpl.class);

	/** 分隔符 */
	private static final String SPLITOR = ".";

	/** methodInfo缓存 */
	private Map<String, ServiceMethodInfo> serviceMethodInfoCache = new ConcurrentHashMap<String, ServiceMethodInfo>();

	/** GatewayServiceConfig缓存 */
	private Map<String, GatewayServiceConfig> gatewayServiceConfigCache = new HashMap<String, GatewayServiceConfig>();

	/** applicationContext */
	private ApplicationContext applicationContext;

	/** 服务提供者的系统名称 */
	private String systemName;

	/** 网关服务实现类所在包 */
	private String basePackages;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isEmpty(systemName)) {
			throw new RuntimeException("systemName can not be empty!");
		}
		if (StringUtils.isBlank(basePackages)) {
			throw new RuntimeException("basePackage can not be blank!");
		}

		autoScan();
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.ServiceMethodInfoRepository#get(java.lang.String)
	 */
	@Override
	public ServiceMethodInfo get(String serviceId) {
		return serviceMethodInfoCache.get(serviceId);
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.ServiceMethodInfoRepository#getServiceInfos()
	 */
	@Override
	public List<GatewayServiceConfig> getServiceMetaInfoList() {
		List<GatewayServiceConfig> result = new ArrayList<GatewayServiceConfig>();
		result.addAll(gatewayServiceConfigCache.values());
		return result;
	}

	/**
	 * 自动扫描
	 * 
	 * @throws SecurityException
	 * @throws Exception
	 * 
	 */
	private void autoScan() throws Exception {
		List<Class<?>> serviceImplementClassList = new ArrayList<Class<?>>();
		// 多个包使用英文,或;或空格分隔
		String[] packages = StringUtils.split(basePackages, ",; ");
		for (String item : packages) {
			item = StringUtils.trimToEmpty(item);
			if (StringUtils.isBlank(item)) {
				continue;
			}
			serviceImplementClassList.addAll(getServiceImplementClassesFromPackage(item));
		}
		for (Class<?> clazz : serviceImplementClassList) {
			Object serviceImplement = applicationContext.getBean(clazz);
			if (serviceImplement == null) {
				throw new RuntimeException("can not find bean of type: " + clazz + " from applicationContext.");
			}
			Class<?> interfaceClass = getServiceInterface(clazz);
			for (Method interfaceMethod : interfaceClass.getMethods()) {
				Method method = serviceImplement.getClass().getDeclaredMethod(interfaceMethod.getName(),
						interfaceMethod.getParameterTypes());
				registerServiceMethod(serviceImplement, interfaceClass, method);
			}
			logger.info("-------------------------\n");// 为查看日志方便做的分隔
		}

		logger.info("service auto scan over! totalCount:{}", serviceMethodInfoCache.size());
	}

	/**
	 * 取得指定包名下的所有带有ServiceInterface注解的网关服务实现类的Class对象列表
	 * 
	 * @param packageName
	 * @return
	 */
	private List<Class<?>> getServiceImplementClassesFromPackage(String packageName) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		List<Class<?>> classes = ClassScanUtil.getClassesFromPackage(packageName);
		for (Class<?> clazz : classes) {
			ServiceInterface serviceInterface = clazz.getAnnotation(ServiceInterface.class);
			if (serviceInterface != null) {
				result.add(clazz);
			}
		}
		logger.info("find {} service implements from package {}.", result.size(), packageName);
		return result;
	}

	/**
	 * 取得指定实现类的网关服务接口类
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getServiceInterface(Class<?> clazz) {
		ServiceInterface serviceInterface = clazz.getAnnotation(ServiceInterface.class);
		Class<?> annotationInterface = serviceInterface.value();
		if (annotationInterface == Void.class) {
			// 当注解中未注明接口类时，自动探测
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces == null || interfaces.length == 0) {
				throw new RuntimeException(clazz.getName() + " does not implement any interface!");
			}
			if (interfaces.length != 1) {
				throw new RuntimeException(clazz.getName()
						+ " implement more than 1 interface, and the value of @ServiceInterface is not set!");
			}
			return interfaces[0];
		}
		return getInterfaceClass(clazz, annotationInterface.getName());
	}

	/**
	 * 取得实现类的指定名称的接口类，如果该类没有实现指定名称的接口则抛出异常
	 *
	 * @param target
	 * @param interfaceName
	 * @return
	 */
	private Class<?> getInterfaceClass(Class<?> clazz, String interfaceName) {
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces == null || interfaces.length == 0) {
			throw new RuntimeException(clazz.getName() + " does not implement interface " + interfaceName);
		}
		Class<?> interfaceClazz = null;
		for (Class<?> item : interfaces) {
			if (item.getName().equals(interfaceName)) {
				interfaceClazz = item;
				break;
			}
		}

		if (null == interfaceClazz) {
			throw new RuntimeException(clazz.getName() + " does not implement interface " + interfaceName);
		}
		return interfaceClazz;
	}

	/**
	 * 注册服务方法
	 * 
	 * @param serviceImplement
	 * @param interfaceClass
	 * @param method
	 */
	protected void registerServiceMethod(Object serviceImplement, Class<?> interfaceClass, Method method) {
		ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
		if (serviceMethod == null) {
			return;
		}
		MethodAccess methodAccess = MethodAccess.get(interfaceClass);
		int index = methodAccess.getIndex(method.getName(), method.getParameterTypes());
		ServiceMethodInfo methodInfo = new ServiceMethodInfo(serviceImplement, methodAccess, index,
				method.getGenericParameterTypes());
		String serviceId = getServiceId(interfaceClass.getName(), method.getName(), methodInfo.getParamTypes());
		// 检查serviceId冲突
		if (serviceMethodInfoCache.containsKey(serviceId)) {
			// 可能是同一个接口两个不同的实现类
			throw new RuntimeException("duplicate serviceId:" + serviceId);
		}
		serviceMethodInfoCache.put(serviceId, methodInfo);

		GatewayServiceConfig gatewayServiceConfig = createGatewayServiceConfig(serviceImplement.getClass(), method,
				serviceId);
		// 检查alias冲突
		if (gatewayServiceConfigCache.containsKey(gatewayServiceConfig.getAlias())) {
			throw new RuntimeException("duplicate alias:" + gatewayServiceConfig.getAlias());
		}
		gatewayServiceConfigCache.put(gatewayServiceConfig.getAlias(), gatewayServiceConfig);

		logger.info("method register success! serviceId:{}", serviceId);
	}

	/**
	 * 创建ServiceMetaInfo
	 * 
	 * @param implementClass
	 * @param method
	 * @param serviceId
	 * @return
	 */
	protected GatewayServiceConfig createGatewayServiceConfig(Class<?> implementClass, Method method,
			String serviceId) {
		ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);

		GatewayServiceConfig gatewayServiceConfig = new GatewayServiceConfig();

		gatewayServiceConfig.setRpcType(serviceMethod.rpcType());
		if (gatewayServiceConfig.getRpcType() == null) {
			throw new RuntimeException("type can not be null! serviceId:" + serviceId);
		}
		String alias = serviceMethod.alias();
		if (StringUtils.isEmpty(alias)) {
			alias = serviceId;
		}
		gatewayServiceConfig.setAlias(alias);
		gatewayServiceConfig.setSystemName(systemName);
		gatewayServiceConfig.setServiceId(serviceId);

		ServiceInterface serviceInterface = implementClass.getAnnotation(ServiceInterface.class);
		gatewayServiceConfig.setProtocol(serviceInterface.protocol());
		if (gatewayServiceConfig.getProtocol() == null) {
			throw new RuntimeException("protocol can not be null! serviceId:" + serviceId);
		}

		gatewayServiceConfig.setNeedLoginBeforeInvoke(serviceMethod.needLogin());
		gatewayServiceConfig.setChangeSessionBeforeInvoke(serviceMethod.changeSession());
		gatewayServiceConfig.setDeleteSessionAfterInvoke(serviceMethod.deleteSession());
		gatewayServiceConfig.setUseETag(serviceMethod.useETag());
		gatewayServiceConfig.setTimeout(serviceMethod.timeout());
		if (gatewayServiceConfig.getTimeout() < 0) {
			throw new RuntimeException("timeout can not less than 0! serviceId:" + serviceId);
		}

		gatewayServiceConfig.setDescription(serviceMethod.description());
		return gatewayServiceConfig;
	}

	/**
	 * 取得服务唯一标识，serviceId会加上方法参数类型摘要前8位，支持方法重载
	 * 
	 * @param interfaceName
	 * @param methodName
	 * @return
	 */
	protected static String getServiceId(String interfaceName, String methodName, Type[] paramTypes) {
		StringBuilder serviceIdBuilder = new StringBuilder(interfaceName).append(SPLITOR).append(methodName)
				.append(SPLITOR);
		StringBuilder builder = new StringBuilder("");
		if (ArrayUtils.isNotEmpty(paramTypes)) {
			for (Type type : paramTypes) {
				builder.append(type.toString());
				// jdk 1.8 开始，Type才有getTypeName方法
				// builder.append(type.getTypeName());
			}
		}
		String base64 = DigestUtils.md5Hex(builder.toString());
		serviceIdBuilder.append(StringUtils.substring(base64, 0, 8));
		return serviceIdBuilder.toString();
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * @param basePackages
	 *            the basePackages to set
	 */
	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

}
