package com.kayak.base.system;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kayak.base.exception.KSystemException;
import com.kayak.base.sql.SqlResult;

public class RequestSupportDb extends RequestSupport {

	/**
	 * 用查询结果集对象转成JSON格式字符串返回
	 * 
	 * @param sqlResult
	 * @return
	 */
	public static String result2JsonList(SqlResult sqlResult) {
		return result2JsonList(new KResult(sqlResult));
	}

	/**
	 * 通用查询结果集对象转成JSON格式字符串返回
	 * 
	 * @param result
	 * @return
	 */
	public static String result2JsonList(KResult result) {
		if (result == null)
			return "";

		JSONObject json = result.getListJson();
		if (json == null)
			json = emptyJsonObject;

		json.put("success", true);

		String strResult = json.toString();
		if (getLocalRequest().getParameter(RequestSupport.DO_NOT_SAY_LOG) == null)
			log.info("##### json list result : " + strResult);

		return strResult;
	}

	/**
	 * 通用查询结果集对象转成树结构JSON格式字符串返回
	 * 
	 * @param result
	 * @return
	 * @throws KSystemException
	 */
	public static String result2JsonTree(KResult result) throws KSystemException {
		if (result == null)
			return "";

		JSONArray tree = result.getTreeJson();
		if (tree == null)
			tree = new JSONArray();

		JSONObject json = new JSONObject();

		json.put("rows", tree);
		json.put("success", true);

		String strResult = json.toString();

		if (getLocalRequest().getParameter(RequestSupport.DO_NOT_SAY_LOG) == null)
			log.info("##### json tree result : " + strResult);

		return strResult;
	}
}
