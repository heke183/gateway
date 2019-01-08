/**
 * 
 */
package com.xianglin.gateway.common.service.spi.model;

import java.lang.reflect.Type;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * 服务方法信息，封装了通过反射调用服务方法时需要的必要信息。
 * 
 * @author pengpeng
 */
public class ServiceMethodInfo {

	/** 反射调用的目标对象，服务实现对象 */
	private Object target;

	/** reflectasm反射调用的MethodAccess对象 */
	private MethodAccess methodAccess;

	/** 方法索引，使用索引而不是方法名调用反射可以提高性能 */
	private int index;

	/** 方法参数类型 */
	private Type[] paramTypes;

	/**
	 * 构造方法
	 * 
	 * @param target
	 * @param methodAccess
	 * @param index
	 * @param paramTypes
	 */
	public ServiceMethodInfo(Object target, MethodAccess methodAccess, int index, Type[] paramTypes) {
		this.target = target;
		this.methodAccess = methodAccess;
		this.index = index;
		this.paramTypes = paramTypes;
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @return the methodAccess
	 */
	public MethodAccess getMethodAccess() {
		return methodAccess;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the paramTypes
	 */
	public Type[] getParamTypes() {
		return paramTypes;
	}
}
