package com.kayak.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.tools.Utils;

/**
 * 用户
 * 
 * @author beacon
 *
 */
@RestController
public class UserInfoController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 登陆获取
	 * 
	 * @param userloginnname
	 * @param userpwd
	 * @return
	 */
	@RequestMapping(value = "/getUserInfoByNamePwd", method = RequestMethod.POST)
	public Map<String, Object> getUserInfoByNamePwd(String userloginnname, String userpwd) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userloginnname", userloginnname);
		paramMap.put("userpwd", userpwd);
		StringBuffer sql = this.getSql(paramMap);
		return this.jdbcTemplate.queryForMap(sql.toString(), paramMap);
	}

	/**
	 * 根据ID获取
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/getUserInfoById", method = RequestMethod.POST)
	public Map<String, Object> getUserInfoById(String userid) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", userid);
		StringBuffer sql = this.getSql(paramMap);
		return this.jdbcTemplate.queryForMap(sql.toString(), paramMap);
	}

	/**
	 * 获取列表
	 * 
	 * @param userloginnname
	 * @param username
	 * @param userdept
	 * @param userstatus
	 * @param usermobileno
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/getUserInfoList", method = RequestMethod.POST)
	public List<Map<String, Object>> getUserInfoList(String userloginnname, String username, String userdept,
			String userstatus, String usermobileno, int pageNumber, int pageSize) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userloginnname", userloginnname);
		paramMap.put("username", username);
		paramMap.put("userdept", userdept);
		paramMap.put("userstatus", userstatus);
		paramMap.put("usermobileno", usermobileno);
		paramMap.put("start", String.valueOf(pageNumber - 1));
		paramMap.put("end", String.valueOf(pageSize));
		StringBuffer sql = this.getSql(paramMap);
		return this.jdbcTemplate.queryForList(sql.toString(), paramMap);
	}

	/**
	 * 保存数据
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
	public int saveUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		jsons.put("userid", Utils.getUUid());
		jsons.put("createdate", Utils.getThisData());
		StringBuffer sql = new StringBuffer(
				"insert into user_info(userid,userloginnname,userpwd,username,userdept,userstatus,useridtype,userridno,"
						+ "usersex,usermobileno,userofficeno,userhomeno,userfaxno,useremail,userpostcode,useraddress,createdate) "
						+ "values (:userid,:userloginnname,:userpwd,:username,:userdept,:userstatus,"
						+ ":useridtype,:userridno,:usersex,:usermobileno,:userofficeno,:userhomeno,:userfaxno,"
						+ ":useremail,:userpostcode,:useraddress,:createdate); ");
		return this.jdbcTemplate.update(sql.toString(), jsons);
	}

	/**
	 * 更新数据
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/UpdataUserInfo", method = RequestMethod.POST)
	public int UpdataUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		jsons.put("modifydate", Utils.getThisData());
		StringBuffer sql = this.getUpdateSql(jsons,new StringBuffer("update user_info modifydate = :modifydate "));
		return this.jdbcTemplate.update(sql.toString(), jsons);
	}
	
	/**
	 * 注销用户
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public int changeStatus(String userid) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", userid);
		paramMap.put("modifydate", Utils.getThisData());
		paramMap.put("userstatus", "D");
		StringBuffer sql = this.getUpdateSql(paramMap,new StringBuffer("update user_info modifydate = :modifydate "));
		return this.jdbcTemplate.update(sql.toString(), paramMap);
	}

	public StringBuffer getUpdateSql(JSONObject jsons, StringBuffer sql) throws JSONException {
		for (Iterator<?> iter = jsons.keys(); iter.hasNext();) {
			String key = iter.next().toString();
			if (key.equals("userid")) {
				continue;
			}
			sql.append("," + key + "=:" + jsons.get(key));
		}
		sql.append(" where userid = :userid");
		return sql;
	}
	
	public StringBuffer getUpdateSql(Map<String,String> params, StringBuffer sql) {
		for (String key:params.keySet()) {
			if (key.equals("userid")) {
				continue;
			}
			sql.append("," + key + "=:" + params.get(key));
		}
		sql.append(" where userid = :userid");
		return sql;
	}
	

	/**
	 * sql 拼接
	 * 
	 * @param paramMap
	 * @return
	 */
	public StringBuffer getSql(Map<String, String> paramMap) {
		StringBuffer sql = new StringBuffer("select * from user_info where 1=1 ");
		if (!StringUtils.isBlank(paramMap.get("userid"))) {
			sql.append("userid = :userid");
		}
		if (!StringUtils.isBlank(paramMap.get("userloginnname"))) {
			sql.append("userloginnname = :userloginnname");
		}
		if (!StringUtils.isBlank(paramMap.get("username"))) {
			sql.append("username = :username");
		}
		if (!StringUtils.isBlank(paramMap.get("userdept"))) {
			sql.append("userdept = :userdept");
		}
		if (!StringUtils.isBlank(paramMap.get("userstatus"))) {
			sql.append("userstatus = :userstatus");
		}
		if (!StringUtils.isBlank(paramMap.get("usersex"))) {
			sql.append("usersex = :usersex");
		}
		if (!StringUtils.isBlank(paramMap.get("usermobileno"))) {
			sql.append("usermobileno = :usermobileno");
		}
		sql.append(" limit :start :end");
		return sql;
	}

}
