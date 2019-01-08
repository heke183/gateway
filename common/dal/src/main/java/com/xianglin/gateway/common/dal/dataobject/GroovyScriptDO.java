/**
 * 
 */
package com.xianglin.gateway.common.dal.dataobject;

/**
 * Groovy脚本DO
 * 
 * @author pengpeng 2016年2月25日下午8:24:40
 */
public class GroovyScriptDO extends BaseDO {

	/** 网关服务配置别名 */
	private String gatewayServiceAlias;

	/** groovy脚本内容 */
	private String script;

	/**
	 * @return the gatewayServiceAlias
	 */
	public String getGatewayServiceAlias() {
		return gatewayServiceAlias;
	}

	/**
	 * @param gatewayServiceAlias
	 *            the gatewayServiceAlias to set
	 */
	public void setGatewayServiceAlias(String gatewayServiceAlias) {
		this.gatewayServiceAlias = gatewayServiceAlias;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

}
