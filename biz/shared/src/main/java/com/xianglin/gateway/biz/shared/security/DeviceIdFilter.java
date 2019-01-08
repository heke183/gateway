/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;

/**
 * deviceId安全过滤器
 * 
 * @author pengpeng 2016年1月19日下午9:39:15
 */
public class DeviceIdFilter implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(DeviceIdFilter.class);

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		// 判断校验deviceId的开关是否关闭
		boolean checkDeviceId = gatewayConfigRepository.get(Constants.SWITCHER_CHECK_DEVICE_ID, false, Boolean.class);
		if (!checkDeviceId) {
			logger.debug("ignore check deviceId! checkDeviceId:{}", checkDeviceId);
			return true;
		}
		if (!checkDeviceId(gatewayRequest)) {
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.PARAM_ERROR));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.PermissionDeny));
			return false;
		}
		return true;
	}

	/**
	 * 执行deviceId检查逻辑
	 * 
	 * @param gatewayRequest
	 * @return
	 */
	private boolean checkDeviceId(GatewayRequest<?> gatewayRequest) {
		String deviceIdFromRequest = gatewayRequest.getDeviceId();
		if (StringUtils.isEmpty(deviceIdFromRequest)) {
			logger.warn("deviceIdFromRequest is empty! gatewayRequest:{}", gatewayRequest);
			return false;
		}
		Session session = gatewayRequest.getSession();
		String deviceIdFromSession = session.getAttribute(Constants.DEVICE_ID);
		if (StringUtils.isEmpty(deviceIdFromSession)) {
			logger.warn("deviceIdFromSession is empty! gatewayRequest:{}", gatewayRequest);
			return false;
		}

		if (!StringUtils.equals(deviceIdFromSession, deviceIdFromRequest)) {
			logger.warn("deviceId not match! deviceIdFromSession:{},deviceIdFromRequest:{}", deviceIdFromSession,
					deviceIdFromRequest);

			return false;
		}
		return true;
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

}
