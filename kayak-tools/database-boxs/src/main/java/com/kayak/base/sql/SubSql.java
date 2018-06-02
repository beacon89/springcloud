package com.kayak.base.sql;

import java.util.Map;

/**
 * 开发人员自定义的子SQL
 * 
 * @author lyg
 * 
 */
public interface SubSql {

	/**
	 * 返回子sql代码
	 * 
	 * @param args
	 *            参数设置时的写入的参数，多个参数使用&分隔
	 * @return
	 */
	public String getSubSql(String[] args, Map<String, Object> params);

}
