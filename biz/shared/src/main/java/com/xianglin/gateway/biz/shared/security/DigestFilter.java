/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;

/**
 * 签名校验器
 * 
 * @author pengpeng 2015年5月4日下午12:06:26
 */
public class DigestFilter implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(DigestFilter.class);

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		// 判断校验请求摘要的开关是否关闭
		boolean checkDigest = gatewayConfigRepository.get(Constants.SWITCHER_CHECK_DIGEST, false, Boolean.class);
		if (!checkDigest) {
			logger.debug("ignore check digest! checkDigest:{}", checkDigest);
			return true;
		}
		if (!verify(gatewayRequest)) {
			// gatewayResponse.setServiceResponse(new
			// ServiceResponse<String>(ResponseEnum.PARAM_ERROR));
			gatewayResponse.setServiceResponse(new ServiceResponse<String>(ResultEnum.SignVerifyFailed));
			return false;
		}
		return true;
	}

	/**
	 * 签名校验
	 * 
	 * @param gatewayRequest
	 * @return
	 */
	public boolean verify(GatewayRequest<?> gatewayRequest) {
		Map<String, String> paramMap = gatewayRequest.getParamMap();
		assert (paramMap != null);
		String digest = paramMap.remove(Constants.REQUEST_DATA_DIGEST);
		if (StringUtils.isEmpty(digest)) {
			logger.warn("digestFromRequest is empty! gatewayRequest:{}", gatewayRequest);
			return false;
		}
		String toBeDigested = gatewayRequest.getRequestData().toString();

		String expectedDigest = Base64.encodeBase64String(DigestUtils.sha512(toBeDigested));

		if (!StringUtils.equals(digest, expectedDigest)) {
			logger.warn("digest not match! expected:{},actual:{}", expectedDigest, digest);
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
