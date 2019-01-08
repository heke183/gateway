/**
 * 
 */
package com.xianglin.gateway.core.service.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.xianglin.gateway.common.dal.daointerface.GroovyScriptDAO;
import com.xianglin.gateway.common.dal.dataobject.GroovyScriptDO;
import com.xianglin.gateway.common.util.GroovyScriptExecutor;
import com.xianglin.gateway.core.model.Constants;
import com.xianglin.gateway.core.service.repository.GatewayConfigRepository;
import com.xianglin.gateway.core.service.repository.GroovyScriptExecutorRepository;

/**
 * Groovy脚本执行器仓储实现类
 * 
 * @author pengpeng 2016年2月25日下午7:10:01
 */
public class GroovyScriptExecutorRepositoryImpl implements GroovyScriptExecutorRepository, InitializingBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GroovyScriptExecutorRepositoryImpl.class);

	/** cache */
	private Map<String, GroovyScriptExecutor> cache = new HashMap<String, GroovyScriptExecutor>();

	/** gatewayConfigRepository */
	private GatewayConfigRepository gatewayConfigRepository;

	/** groovyScriptDAO */
	private GroovyScriptDAO groovyScriptDAO;

	/** systemName */
	private String systemName;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		refresh();
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GroovyScriptExecutorRepository#refresh()
	 */
	@Override
	public synchronized void refresh() {
		gatewayConfigRepository.refresh();
		boolean useGroovy = gatewayConfigRepository.get(Constants.SWITCHER_USE_GROOVY, false, Boolean.class);
		if (!useGroovy) {
			logger.info("skip load GroovyScript! useGroovy:{}", useGroovy);
			cache = new HashMap<String, GroovyScriptExecutor>();
			return;
		}

		Map<String, GroovyScriptExecutor> map = new HashMap<String, GroovyScriptExecutor>();
		List<GroovyScriptDO> list = groovyScriptDAO.getAllEnable(systemName);
		for (GroovyScriptDO item : list) {
			if (StringUtils.isEmpty(item.getScript())) {
				logger.warn("skip empty groovy script! groovyScriptDO:" + item);
				continue;
			}
			GroovyScriptExecutor executor = null;
			try {
				executor = new GroovyScriptExecutor(item.getScript());
			} catch (Throwable e) {
				logger.error("refresh groovy script error! groovyScriptDO:" + item, e);
				return;
			}
			logger.info("load GroovyScript:{}", item);
			map.put(item.getGatewayServiceAlias(), executor);
		}

		logger.info("load GroovyScript over! size:{}", map.size());

		cache = map;
	}

	/**
	 * @see com.xianglin.gateway.core.service.repository.GroovyScriptExecutorRepository#get(java.lang.String)
	 */
	@Override
	public GroovyScriptExecutor get(String operation) {
		return cache.get(operation);
	}

	/**
	 * @param gatewayConfigRepository
	 *            the gatewayConfigRepository to set
	 */
	public void setGatewayConfigRepository(GatewayConfigRepository gatewayConfigRepository) {
		this.gatewayConfigRepository = gatewayConfigRepository;
	}

	/**
	 * @param groovyScriptDAO
	 *            the groovyScriptDAO to set
	 */
	public void setGroovyScriptDAO(GroovyScriptDAO groovyScriptDAO) {
		this.groovyScriptDAO = groovyScriptDAO;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
