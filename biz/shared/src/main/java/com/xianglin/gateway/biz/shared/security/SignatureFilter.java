/**
 * 
 */
package com.xianglin.gateway.biz.shared.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.model.GatewayRequest;
import com.xianglin.gateway.core.model.GatewayResponse;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;
import com.xianglin.gateway.core.service.signature.SignatureService;

/**
 * 签名校验器
 * 
 * @author pengpeng 2015年5月4日下午12:06:26
 */
public class SignatureFilter implements SecurityFilter {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SignatureFilter.class);

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/** signatureService */
	private SignatureService signatureService;

	/**
	 * @see com.xianglin.gateway.biz.shared.security.SecurityFilter#doFilter(com.xianglin.gateway.core.model.GatewayRequest,
	 *      com.xianglin.gateway.core.model.GatewayResponse)
	 */
	@Override
	public boolean doFilter(GatewayRequest<?> gatewayRequest, GatewayResponse gatewayResponse) {
		// 判断校验签名的开关是否关闭
		boolean checkSignature = gatewayConfigRepository.get(Constants.SWITCHER_CHECK_SIGNATURE, false, Boolean.class);
		if (!checkSignature) {
			logger.debug("ignore check signature! checkSignature:{}", checkSignature);
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
	 * 执行签名校验逻辑
	 * 
	 * @param paramMap
	 * @return
	 */
	public boolean verify(GatewayRequest<?> gatewayRequest) {
		Map<String, String> paramMap = gatewayRequest.getParamMap();
		String signature = paramMap.remove(Constants.SIGNATURE);
		if (StringUtils.isEmpty(signature)) {
			logger.warn("signatureFromRequest is empty! gatewayRequest：{}", gatewayRequest);
			return false;
		}
		String toBeSigned = getToBeSigned(paramMap);
		boolean success = false;
		try {
			success = signatureService.verify(toBeSigned, signature);
		} catch (Throwable e) {
			logger.error("signature verify error! ", e);
			return false;
		}
		return success;
	}

	/**
	 * 取得待签名内容
	 * 
	 * @param paramMap
	 * @return
	 */
	private String getToBeSigned(Map<String, String> paramMap) {
		List<String> paramNameList = new ArrayList<String>(paramMap.keySet());
		Collections.sort(paramNameList);
		StringBuffer buffer = new StringBuffer();
		String paramName = null;
		for (Iterator<String> iterator = paramNameList.iterator(); iterator.hasNext();) {
			paramName = iterator.next();
			buffer.append(paramName).append("=").append(paramMap.get(paramName));
			if (iterator.hasNext()) {
				buffer.append("&");
			}
		}
		return buffer.toString();
	}

	/**
	 * @param signatureService
	 *            the signatureService to set
	 */
	public void setSignatureService(SignatureService signatureService) {
		this.signatureService = signatureService;
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

}
