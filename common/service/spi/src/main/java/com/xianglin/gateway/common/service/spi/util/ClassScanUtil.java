/**
 * 
 */
package com.xianglin.gateway.common.service.spi.util;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类扫描工具类
 * 
 * @author pengpeng 2015年9月7日下午12:53:53
 */
public class ClassScanUtil {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(ClassScanUtil.class);

	/** url protocol: jar */
	public static final String URL_PROROCOL_JAR = "jar";

	/** url protocol: file */
	public static final String URL_PROTOCOL_FILE = "file";

	/** 包路径分隔符 */
	public static final String SPLITOR_PACKAGE = ".";

	/** 文件路径分隔符 */
	public static final String SPLITOR_FILE_PATH = "/";

	/** window系统文件路径分隔符 */
	public static final String SPLITOR_FILE_PATH_WINDOWS = "\\";

	/** class文件后缀名 */
	public static final String CLASS_FILE_SUFFIX = ".class";

	/** url decode 字符集 */
	public static final String URL_DECODE_CHARSET = "UTF-8";

	/**
	 * 扫描并返回指定包名下所有的类的Class对象集合
	 * 
	 * @param packageName
	 * @return
	 */
	public static List<Class<?>> getClassesFromPackage(String packageName) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		if (StringUtils.isBlank(packageName)) {
			throw new IllegalArgumentException("packageName is blank!");
		}
		String packageDir = StringUtils.replace(packageName, SPLITOR_PACKAGE, SPLITOR_FILE_PATH);
		Enumeration<URL> urlEnumeration = getURLEnumerationFromPackage(packageDir);
		if (urlEnumeration == null) {
			return result;
		}
		while (urlEnumeration.hasMoreElements()) {
			URL url = urlEnumeration.nextElement();
			String protocol = url.getProtocol();
			if (StringUtils.equals(protocol, URL_PROROCOL_JAR)) {
				result.addAll(getClassesFromJarFile(url, packageDir, true));
			} else if (StringUtils.equals(protocol, URL_PROTOCOL_FILE)) {
				result.addAll(getClassesFromFilePath(url, packageDir, true));
			} else {
				logger.error("getClassesFromPackage, not supported url protocol:" + protocol);
			}
		}

		return result;
	}

	/**
	 * 取出包名下的所有资源URL的枚举器
	 * 
	 * @param packageDir
	 * @return
	 */
	public static Enumeration<URL> getURLEnumerationFromPackage(String packageDir) {
		Enumeration<URL> result = null;
		if (StringUtils.isBlank(packageDir)) {
			throw new IllegalArgumentException("packageDir is blank!");
		}
		try {
			result = Thread.currentThread().getContextClassLoader().getResources(packageDir);
		} catch (Throwable e) {
			logger.error("getURLEnumerationFromPackage error!", e);
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * 取出jar包中指定包路径下的所有类的Class对象集合
	 * 
	 * @param jarFileURL
	 * @param packageDir
	 * @param recursive
	 * @return
	 */
	public static List<Class<?>> getClassesFromJarFile(URL jarFileURL, String packageDir, boolean recursive) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		if (jarFileURL == null) {
			throw new NullPointerException("jarFileURL is null!");
		}
		if (!StringUtils.equals(jarFileURL.getProtocol(), URL_PROROCOL_JAR)) {
			throw new IllegalArgumentException("not a legal jarFileURL:" + jarFileURL);
		}
		if (StringUtils.isBlank(packageDir)) {
			throw new NullPointerException("packageDir is blank!");
		}
		Enumeration<JarEntry> jarEntryEnumeration = getJarEntryEnumeration(jarFileURL);
		if (jarEntryEnumeration == null) {
			return result;
		}
		while (jarEntryEnumeration.hasMoreElements()) {
			JarEntry jarEntry = jarEntryEnumeration.nextElement();
			String name = jarEntry.getName();

			if (StringUtils.startsWith(name, packageDir) && StringUtils.endsWith(name, CLASS_FILE_SUFFIX)) {
				if (StringUtils.lastIndexOf(name, SPLITOR_FILE_PATH) != packageDir.length() && !recursive) {
					// 遇到packageDir的子目录下的class文件
					// 如果不递归（即：只取packageDir下的class文件，不取子目录下的class），则跳过
					continue;
				}
				String classFullName = StringUtils.substring(name, 0, StringUtils.indexOf(name, CLASS_FILE_SUFFIX));
				classFullName = StringUtils.replace(classFullName, SPLITOR_FILE_PATH, SPLITOR_PACKAGE);
				Class<?> clazz = getClass(classFullName);
				if (clazz != null) {
					result.add(clazz);
				}
			}
		}
		return result;
	}

	/**
	 * 取得jar包中所有JarEntry的枚举器
	 * 
	 * @param jarFileURL
	 * @return
	 */
	public static Enumeration<JarEntry> getJarEntryEnumeration(URL jarFileURL) {
		JarFile jarFile = null;
		try {
			jarFile = ((JarURLConnection) jarFileURL.openConnection()).getJarFile();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
		return jarEntryEnumeration;
	}

	/**
	 * @param filePathURL
	 * @param packageDir
	 * @param recursive
	 * @return
	 */
	public static List<Class<?>> getClassesFromFilePath(URL filePathURL, String packageDir, boolean recursive) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		if (filePathURL == null) {
			throw new NullPointerException("filePathURL is null!");
		}
		if (!StringUtils.equals(filePathURL.getProtocol(), URL_PROTOCOL_FILE)) {
			throw new NullPointerException("not a legal filePathURL:" + filePathURL);
		}
		if (StringUtils.isBlank(packageDir)) {
			throw new NullPointerException("packageDir is blank!");
		}
		String packagePath = decodeURL(filePathURL); // 防止classPath中有非英文路径名？？？
		if (StringUtils.isBlank(packagePath)) {
			return result;
		}

		List<File> classFiles = getClassFiles(new File(packagePath), recursive);
		for (File file : classFiles) {
			String filePath = StringUtils.replace(file.getPath(), SPLITOR_FILE_PATH_WINDOWS, SPLITOR_FILE_PATH);
			int start = StringUtils.indexOf(filePath, packageDir);
			int end = StringUtils.indexOf(filePath, CLASS_FILE_SUFFIX);
			String classFullName = StringUtils.substring(filePath, start, end);
			classFullName = StringUtils.replace(classFullName, SPLITOR_FILE_PATH, SPLITOR_PACKAGE);
			Class<?> clazz = getClass(classFullName);
			if (clazz != null) {
				result.add(clazz);
			}
		}
		return result;
	}

	/**
	 * 取得指定路径下的所有.class文件集合
	 * 
	 * @param filePath
	 *            指定路径
	 * @param recursive
	 *            是否级联
	 * @return
	 */
	public static List<File> getClassFiles(File filePath, final boolean recursive) {
		List<File> result = new ArrayList<File>();
		if (filePath == null || !filePath.exists() || !filePath.isDirectory()) {
			return result;
		}
		File[] children = filePath.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (StringUtils.endsWith(file.getName(), CLASS_FILE_SUFFIX)) {
					return true;
				}
				if (recursive && file.isDirectory()) {
					return true;
				}
				return false;
			}
		});

		for (File child : children) {
			if (child.isDirectory()) {
				result.addAll(getClassFiles(child, recursive));
			} else {
				result.add(child);
			}
		}
		return result;
	}

	/**
	 * 加载指定全限定名的类，并得到该类的Class对象
	 * 
	 * @param classFullName
	 * @return
	 */
	public static Class<?> getClass(String classFullName) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(classFullName);
		} catch (Throwable e) {
			logger.error("getClass error!", e);
			throw new RuntimeException(e);
		}
		return clazz;
	}

	/**
	 * @param url
	 * @param packagePath
	 * @return
	 */
	private static String decodeURL(URL url) {
		String result = null;
		try {
			result = URLDecoder.decode(url.getFile(), URL_DECODE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			logger.error("decodeURL error! url:" + url, e);
			throw new RuntimeException(e);
		}
		return result;
	}
}
