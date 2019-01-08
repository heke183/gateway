/**
 * 
 */
package com.xianglin.gateway.common.service.spi.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.xianglin.gateway.common.service.spi.ExceptionProcessor;
import com.xianglin.gateway.common.service.spi.ParamProcessor;
import com.xianglin.gateway.common.service.spi.ResponseProcessor;
import com.xianglin.gateway.common.service.spi.ServiceMethodInfoRepository;
import com.xianglin.gateway.common.service.spi.exception.ServiceMethodNotFoundException;
import com.xianglin.gateway.common.service.spi.model.GatewayServiceConfig;
import com.xianglin.gateway.common.service.spi.model.ServiceMethodInfo;
import com.xianglin.gateway.common.service.spi.model.ServiceRequest;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.common.service.spi.util.GatewayRequestContext;

/**
 * 通用网关服务实现类，可继承该类并覆盖某些方法。
 * 
 * @author pengpeng
 */
public abstract class BaseGatewayService<I, O> implements InitializingBean {

	/** logger */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** 服务方法注册器 */
	protected ServiceMethodInfoRepository serviceMethodInfoRepository;

	/** 参数处理器 */
	protected ParamProcessor<I> paramProcessor;

	/** 响应处理器 */
	protected ResponseProcessor<I, O> responseProcessor;

	/** 异常处理器 */
	protected ExceptionProcessor<I, O> exceptionProcessor = new DefaultExceptionProcessor<I, O>();

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GatewayServiceConfig> result = serviceMethodInfoRepository.getServiceMetaInfoList();
		for (GatewayServiceConfig item : result) {
			item.setSpiVersion(getVersion());
			item.setGatewayServiceInterface(getGatewayServiceInterface());
		}
	}

	/**
	 * 取得服务版本号
	 * 
	 * @return
	 */
	public abstract String getVersion();

	/**
	 * 取得服务接口名称
	 * 
	 * @return
	 */
	public abstract Class<?> getGatewayServiceInterface();

	/**
	 * @see com.xianglin.gateway.common.service.spi.GatewayService#service(com.xianglin.gateway.common.service.spi.model.ServiceRequest)
	 */
	public ServiceResponse<O> service(ServiceRequest<I> request) {
		logger.debug("service start, request:{}", request);

		GatewayRequestContext.setSessionId(request.getSessionId());
		GatewayRequestContext.setRequestProperties(request.getProperties());

		ServiceResponse<O> result = doService(request.getServiceId(), request.getRequestData());

		GatewayRequestContext.clear();

		logger.debug("service end, response:{}", result);
		return result;
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.GatewayService#oneway(com.xianglin.gateway.common.service.spi.model.ServiceRequest)
	 */
	public void oneway(ServiceRequest<I> request) {
		logger.debug("onewayservice start, request:{}", request);

		GatewayRequestContext.setSessionId(request.getSessionId());
		GatewayRequestContext.setRequestProperties(request.getProperties());

		doOneway(request.getServiceId(), request.getRequestData());

		GatewayRequestContext.clear();

		logger.debug("onewayservice end.");
	}

	/**
	 * @see com.xianglin.gateway.common.service.spi.GatewayService#getGatewayServiceConfigList()
	 */
	public List<GatewayServiceConfig> getGatewayServiceConfigList() {
		return serviceMethodInfoRepository.getServiceMetaInfoList();
	}

	/**
	 * 通过反射调用业务服务
	 * 
	 * @param serviceId
	 * @param requestData
	 * @return
	 */
	protected ServiceResponse<O> doService(String serviceId, I requestData) {
		ServiceResponse<O> response = null;
		ServiceMethodInfo serviceMethodInfo = serviceMethodInfoRepository.get(serviceId);
		if (serviceMethodInfo == null) {
			logger.error("can not find methodInfo:" + serviceId);
			// 抛出异常给网关，由网关来处理，可能是网关配置错误，这是唯一一个需要抛给网关的异常
			throw new ServiceMethodNotFoundException(serviceId);
		}
		try {
			// 解析参数
			Object[] params = paramProcessor.process(serviceId, requestData, serviceMethodInfo.getParamTypes());

			// 通过reflectasm调用方法，服务方法内部需要捕获异常，并返回合理的结果。
			Object result = serviceMethodInfo.getMethodAccess().invoke(serviceMethodInfo.getTarget(),
					serviceMethodInfo.getIndex(), params);

			// 响应结果转换
			response = responseProcessor.process(serviceId, result);
		} catch (Throwable e) {
			logger.error("doService error! serviceId:" + serviceId + ", requestData:" + requestData, e);
			response = processException(serviceId, e);
		}

		// 禁止返回null
		if (response == null) {
			logger.error("serviceResponse can not be null!");
			// response = new ServiceResponse<O>(ResponseEnum.SERVICE_ERROR);
			response = new ServiceResponse<O>(ResultEnum.BizException);
		}
		return response;
	}

	/**
	 * 通过反射调用业务服务
	 * 
	 * @param serviceId
	 * @param requestData
	 */
	protected void doOneway(String serviceId, I requestData) {
		ServiceMethodInfo serviceMethodInfo = serviceMethodInfoRepository.get(serviceId);
		if (serviceMethodInfo == null) {
			logger.error("can not find methodInfo:" + serviceId);
			// 抛出异常给网关，由网关来处理，可能是网关配置错误，这是唯一一个需要抛给网关的异常
			throw new ServiceMethodNotFoundException(serviceId);
		}
		try {
			// 解析参数
			Object[] params = paramProcessor.process(serviceId, requestData, serviceMethodInfo.getParamTypes());

			// 通过reflectasm调用方法，服务方法内部需要捕获异常。
			serviceMethodInfo.getMethodAccess().invoke(serviceMethodInfo.getTarget(), serviceMethodInfo.getIndex(),
					params);
		} catch (Throwable e) {
			logger.error("doService error! serviceId:" + serviceId + ", requestData:" + requestData, e);
			processException(serviceId, e);
		}
	}

	/**
	 * 处理异常
	 * 
	 * @param serviceId
	 * @param requestData
	 * @param e
	 * @return
	 */
	protected ServiceResponse<O> processException(String serviceId, Throwable e) {
		ServiceResponse<O> response = null;
		try {
			response = exceptionProcessor.process(serviceId, e);
		} catch (Throwable ex) {
			logger.error("processException error! serviceId:" + serviceId, ex);
			// response = new ServiceResponse<O>(ResponseEnum.SERVICE_ERROR);
			response = new ServiceResponse<O>(ResultEnum.BizException);
		}
		return response;
	}

	/**
	 * @param serviceMethodInfoRepository
	 *            the serviceMethodInfoRepository to set
	 */
	public void setServiceMethodInfoRepository(ServiceMethodInfoRepository serviceMethodInfoRepository) {
		this.serviceMethodInfoRepository = serviceMethodInfoRepository;
	}

	/**
	 * @param paramProcessor
	 *            the paramProcessor to set
	 */
	public void setParamProcessor(ParamProcessor<I> paramProcessor) {
		this.paramProcessor = paramProcessor;
	}

	/**
	 * @param responseProcessor
	 *            the responseProcessor to set
	 */
	public void setResponseProcessor(ResponseProcessor<I, O> responseProcessor) {
		this.responseProcessor = responseProcessor;
	}

	/**
	 * @param exceptionProcessor
	 *            the exceptionProcessor to set
	 */
	public void setExceptionProcessor(ExceptionProcessor<I, O> exceptionProcessor) {
		this.exceptionProcessor = exceptionProcessor;
	}

}
