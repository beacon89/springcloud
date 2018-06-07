package com.kayak.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class BaseController {

	/**
	 * 查询拼接
	 * 
	 * @param tablename
	 *            表明
	 * @param jsons
	 *            查询条件
	 * @param pageNumber
	 *            开始
	 * @param end
	 *            终止
	 * @param flag
	 *            是否分页
	 * @param queryList
	 *            获取值
	 * @return
	 * @throws JSONException
	 */
	public StringBuffer getQuerySql(String tablename, JSONObject jsons, int pageNumber, int pageSize, boolean flag,
			List<Object> paramslist) throws JSONException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(tablename);
		sql.append(" where 1=1 ");
		for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			if (jsons.isNull(key) || StringUtils.isBlank(jsons.get(key).toString())) {
				continue;
			}
			sql.append(" and " + key + "= ?");
			paramslist.add(jsons.get(key));

		}
		if (flag == false) {
			return sql;
		}
		int start = (pageNumber - 1) == 0 ? 0 : (pageNumber - 1) * pageSize;
		int end = (pageNumber - 1) == 0 ? pageSize : pageNumber * pageSize;
		sql.append(" limit ");
		sql.append(start);
		sql.append(",");
		sql.append(end);
		return sql;
	}

	/**
	 * 更新拼接
	 * 
	 * @param tablename
	 *            表明
	 * @param dataname
	 *            固定更新字段名称
	 * @param datavalue
	 *            固定更新的值
	 * @param jsons
	 *            提交参数
	 * @param queryList
	 *            获取值
	 * @param ids
	 *            依据更新的条件
	 * @return
	 * @throws JSONException
	 */
	public StringBuffer getUpdateSql(String tablename, String dataname, String datavalue, JSONObject jsons,List<Object> paramslist, String... ids) throws JSONException {
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tablename);
		sql.append(" set " + dataname + "= ? ");
		paramslist.add(datavalue);
		outer: for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			for (String str : ids) {
				if (key.equals(str)) {
					continue outer;
				}
			}
			sql.append("," + key + "= ?");
			paramslist.add(jsons.get(key));
		}
		sql.append(" where ");
		int j = 0;
		for (String key : ids) {
			if (j > 1) {
				sql.append(" and " + key + "= ?");
			} else {
				sql.append(key + "= ?");
			}
			j++;
			paramslist.add(jsons.get(key));
		}
		return sql;
	}
	
	public StringBuffer getUpdateSql(String tablename,Map<String,String> mustmap, JSONObject jsons,List<Object> paramslist, String... ids) throws JSONException {
		StringBuffer sql = new StringBuffer("update ");
		sql.append(tablename);
		sql.append(" set ");
		int j = 0;
		Iterator<Map.Entry<String, String>> it = mustmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if(j == 0) {
				sql.append(entry.getKey()+"=?");
			}else {
				sql.append(","+entry.getKey()+"=?");
			}
			j++;
			paramslist.add(entry.getValue());
		}
		outer: for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			for (String str : ids) {
				if (key.equals(str)) {
					continue outer;
				}
			}
			sql.append("," + key + "= ?");
			paramslist.add(jsons.get(key));
		}
		sql.append(" where ");
		int k = 0;
		for (String key : ids) {
			if (k > 1) {
				sql.append(" and " + key + "= ?");
			} else {
				sql.append(key + "= ?");
			}
			k++;
			paramslist.add(jsons.get(key));
		}
		return sql;
	}

	/**
	 * 查询数据个数
	 * 
	 * @param tablename
	 * @return
	 */
	public StringBuffer getQueryCount(String tablename) {
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tablename);
		return sql;
	}
	
	
	public StringBuffer getQueryCheck(String tablename,JSONObject jsons,List<Object> paramslist) throws JSONException {
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tablename);
		sql.append(" where 1=1 ");
		for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			sql.append(" and " + key + "= ?");
			paramslist.add(jsons.get(key));
		}
		return sql;
	}
	
	/**
	 * 状态为 0000为成功
	 * @param map
	 * @return
	 */
	public Map<String, Object> responseMessage(Map<String, Object> map) {
		if(map == null) {
			map = new HashMap<>();
		}
		map.put("returnState", TransactionCode.CODE0000.getCode());
		return map;
	}
	
	public Map<String, Object> responseMessage(String code,String message) {
		Map<String, Object> map = new HashMap<>();
		map.put("returnState", code);
		map.put("returnMsg",message );
		return map;
	}

	/**
	 * 查询固定封装返回给前端用的
	 * 
	 * @param list
	 *            返回结果
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            一页多少条
	 * @param totalCount
	 *            总数
	 * @return
	 */
	public Map<String, Object> responseMessage(List<?> list, long pageNumber, long pageSize,long totalCount,Map<String, Object> map) {
		if(map == null) {
			map = new HashMap<>();
		}
		map.put("detail", list);
		map.put("pageNumber", pageNumber);
		map.put("pageSize", pageSize);
		map.put("totalCount", totalCount);
		return this.responseMessage(map);
	}
	
	public Map<String, Object> responseMessage(int count,Map<String, Object> map){
		if(map == null) {
			map = new HashMap<>();
		}
		map.put("detail", count);
		return this.responseMessage(map);
	}
	
	
	public StringBuffer getDelSql(String tablename,JSONObject jsons,List<Object> paramslist,String... keys) throws JSONException {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(tablename);
		sql.append(" where ");
		int i = 0;
		for(String key:keys) {
			if(i == 0) {
				sql.append(" "+key+"=?");
				paramslist.add(jsons.get(key));
				continue;
			}
			i++;
			sql.append(" and "+key+"=?");
			paramslist.add(jsons.get(key));
		}
		return sql;
	}
	
	public StringBuffer getInsertSql(String tablename, Map<String,String> params, JSONObject jsons,List<Object> paramslist) throws JSONException {
		StringBuffer sql = new StringBuffer("insert into  ");
		sql.append(tablename);
		sql.append("(");
		int j = 0;
		for(String key:params.keySet()) {
			if(j==0) {
				sql.append(key);
			}else {
				sql.append("," + key + "");
			}
			j++;
			paramslist.add(params.get(key));
		}
		for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			sql.append("," + key + "");
			paramslist.add(jsons.get(key));
		}
		sql.append(" ) values ( ");
		for (int i = 1; i <= paramslist.size(); i++) {
			if (i == paramslist.size()) {
				sql.append("?");
				continue;
			}
			sql.append("?,");
		}
		sql.append(")");
		return sql;
	}
	

	public StringBuffer getInsertSql(String tablename, String idname, String idvalue, JSONObject jsons,List<Object> paramslist) throws JSONException {
		StringBuffer sql = new StringBuffer("insert into  ");
		sql.append(tablename);
		sql.append("(" + idname);
		paramslist.add(idvalue);
		for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			sql.append("," + key + "");
			paramslist.add(jsons.get(key));
		}
		sql.append(" ) values ( ");
		for (int i = 1; i <= paramslist.size(); i++) {
			if (i == paramslist.size()) {
				sql.append("?");
				continue;
			}
			sql.append("?,");
		}
		sql.append(")");
		return sql;
	}
	

}
