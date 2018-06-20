package com.kayak.base.system;

import org.apache.tomcat.jni.Global;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.SequenceDao;
import com.kayak.base.service.ComnService;
import com.kayak.base.spring.SpringContextHolder;
import com.kayak.base.sql.SysSql;

/**
 * 用于通过Spring框架静态取得定义好的bean对象实例
 */
public class SysBeans {
	/**
	 * 静态获取bean实例
	 * 
	 * @param beanId
	 * @return
	 */
	public static <T> T getBean(String beanId) {
		return SpringContextHolder.getBean(beanId);
	}

	/**
	 * 取得全局配置信息的实例（静态）
	 * 
	 * @return Global
	 */
	public static Global getGlobal() {
		return getBean("global");
	}

	/**
	 * 取得sequencdDao的实例<br />
	 * 因为sequenceDao是静态的，所以不必担心上下文问题
	 */
	public static SequenceDao getSequenceDao() {
		return getBean("sequenceDao");
	}

	/**
	 * 取id=comnService的bean实例<br />
	 * 通用查询对象，用于执行通用查询
	 */
	public static ComnService getComnService() {
		ComnService service = getBean("comnService");
		return service;
	}

	/**
	 * 返回提供执行SQL方法的对象<br />
	 * 如果有参数需要注入，请使用setParams方法设置参数对象
	 * 
	 * @return
	 */
	public static ComnDao getComnDao() {
		ComnDao service = getBean("comnDao");
		return service;
	}

	/**
	 * 取id=sysSql的bean实例<br />
	 * 系统SQL配置管理器
	 * 
	 * @return
	 */
	public static SysSql getSysSql() {
		return getBean("sysSql");
	}

}
