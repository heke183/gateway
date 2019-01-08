/**
 * 
 */
package com.xianglin.gateway.common.service.spi.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * ClassUtil单元测试
 * 
 * @author pengpeng 2015年9月7日下午3:08:13
 */
public class ClassScanUtilTest {

	@Test
	public void testGetClassesFromPackage() {
		String packageName = "org.apache.commons.lang3";
		List<Class<?>> classes = ClassScanUtil.getClassesFromPackage(packageName);
		Assert.assertTrue(classes.size() > 0);
	}

	@Test
	public void testGetClassesFromPackage2() {
		String packageName = "com.xianglin.gateway";
		List<Class<?>> classes = ClassScanUtil.getClassesFromPackage(packageName);
		Assert.assertTrue(classes.size() > 0);
	}

}
