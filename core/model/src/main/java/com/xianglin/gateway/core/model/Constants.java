/**
 * 
 */
package com.xianglin.gateway.core.model;

/**
 * 常量类
 * 
 * @author pengpeng
 */
public class Constants {

	/** 设备唯一标识 */
	public static final String DEVICE_ID = "did";

	public static final String USER_INFO = "partyId";

	public static final String SIGNATURE = "s";

	public static final String REQUEST_DATA_DIGEST = "d";

	// ---------------------

	/** 开关名称：是否校验deviceId */
	public static final String SWITCHER_CHECK_DEVICE_ID = "checkDeviceId";

	/** 开关名称：是否校验摘要 */
	public static final String SWITCHER_CHECK_DIGEST = "checkDigest";

	/** 开关名称：是否校验签名 */
	public static final String SWITCHER_CHECK_SIGNATURE = "checkSignature";

	/** 开关名称：是否使用groovy */
	public static final String SWITCHER_USE_GROOVY = "useGroovy";

	/** 开关名称：是否使用Session */
	public static final String SWITCHER_USE_SESSION = "useSession";

	// --------------------

	/** groovy binding name:gatewayRequest */
	public static final String GROOVY_GATEWAY_REQUEST = "gatewayRequest";

	/** groovy binding name:gatewayResponse */
	public static final String GROOVY_GATEWAY_RESPONSE = "gatewayResponse";

	/** groovy binding name:gatewayServiceInvoker */
	public static final String GROOVY_GATEWAY_SERVICE_INVOKER = "gatewayServiceInvoker";

	/** groovy binding name:logger */
	public static final String GROOVY_LOGGER = "logger";

}
