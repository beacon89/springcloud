package com.kayak.base.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class.getName());

	/**
	 * 获取资源文件列表
	 * 
	 * @param pattern
	 * @return
	 */
	public static Resource[] getResources(String pattern) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		try {
			return resolver.getResources(pattern);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取文件输入流
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream getFileInputStream(String path) {
		Resource resource = new ClassPathResource(path);

		if (resource.exists()) {
			try {
				return resource.getInputStream();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

}
