/**
 * 
 */
package com.xianglin.gateway.common.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

/**
 * groovy脚本执行器
 * 
 * @author pengpeng 2016年2月25日下午5:53:29
 */
public class GroovyScriptExecutor {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(GroovyScriptExecutor.class);

	/** script */
	private String script;

	/** scriptClass */
	private Class<Script> scriptClass;

	/**
	 * @param script
	 */
	@SuppressWarnings("unchecked")
	public GroovyScriptExecutor(String script) {
		this.script = script;
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
		scriptClass = (Class<Script>) groovyClassLoader.parseClass(script);
		try {
			groovyClassLoader.close();
		} catch (IOException e) {
			logger.error("close groovyClassLoader error!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行脚本
	 * 
	 * @param bindingMap
	 * @return
	 */
	public Object execute(Map<String, Object> bindingMap) {
		Script script = null;
		try {
			script = scriptClass.newInstance();
		} catch (Throwable e) {
			logger.error("new Script instance error!", e);
			throw new RuntimeException(e);
		}

		Binding binding = new Binding();
		if (bindingMap != null && bindingMap.size() > 0) {
			for (String key : bindingMap.keySet()) {
				binding.setVariable(key, bindingMap.get(key));
			}
		}
		script.setBinding(binding);
		return script.run();
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

}
