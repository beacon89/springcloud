package com.kayak.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class UserInfoController extends BaseController{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String tablename = "user_info";
	
	
	/**
	 * 查询user
	 * @param json 提交条件
	 * @param pageNumber 页码
	 * @param pageSize 一页多少条
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/getUserInfoList",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoList(String json, int pageNumber, int pageSize) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getQuerySql(this.tablename, jsons, pageNumber, pageSize, true, paramslist);
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql.toString(),paramslist.toArray());
		return this.responseMessage(list, pageNumber, pageSize, this.getUserInfoTotalCounts(),null);
	}
	
	private int getUserInfoTotalCounts() {
		return this.jdbcTemplate.queryForObject(this.getQueryCount(this.tablename).toString(), Integer.class);
	}
	
	/**
	 * 查询总记录数
	 * @return
	 */
	@RequestMapping(value = "/getUserInfoTotalCount",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoTotalCount() {
		return this.responseMessage(this.getUserInfoTotalCounts(),null);
	}
	
	
	@RequestMapping(value = "/checkUserInfoLogname",method = RequestMethod.POST)
	public Map<String, Object> getCheckUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getQueryCheck(this.tablename, jsons,paramslist);
		return this.responseMessage(this.jdbcTemplate.queryForObject(sql.toString(), paramslist.toArray(),Integer.class),null);
	}
	
	/**
	 * 更新数据
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/UpdataUserInfo",method = RequestMethod.POST)
	public Map<String, Object> UpdataUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		jsons.put("modifydate", Utils.getThisData());
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getUpdateSql("user_info", "modifydate", Utils.getThisData(), jsons, paramslist, "userid");
		return this.responseMessage(this.jdbcTemplate.update(sql.toString(), paramslist.toArray()),null);
	}
	
	
	/**
	 * 登陆获取
	 * 
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/getUserInfoByNamePwd",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoByNamePwd(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getQuerySql(this.tablename, jsons, 0, 0, false, paramslist);
		return this.responseMessage(this.jdbcTemplate.queryForMap(sql.toString(),paramslist.toArray()));
	}

	/**
	 * 根据ID获取
	 * 
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/getUserInfoById",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoById(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getQuerySql(this.tablename, jsons, 0, 0, false, paramslist);
		return this.responseMessage(this.jdbcTemplate.queryForMap(sql.toString(), paramslist.toArray()));
	}
	
	

	
	

	/**
	 * 保存数据
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/saveUserInfo",method = RequestMethod.POST)
	public Map<String, Object> saveUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userid", Utils.getUUid());
		params.put("createdate", Utils.getThisData());
		List <Object> paramslist=new  ArrayList<Object>();  
		StringBuffer sql = this.getInsertSql(this.tablename, params, jsons, paramslist);
		return this.responseMessage(this.jdbcTemplate.update(sql.toString(), paramslist.toArray()),null);
	}

	
	
	/**
	 * 注销用户
	 * @param userid
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
	public Map<String, Object> changeStatus(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		HashMap<String, String> mustmap = new HashMap<String, String>();
		mustmap.put("modifydate", Utils.getThisData());
		mustmap.put("userstatus", "D");
		List <Object> paramslist = new  ArrayList<Object>();  
		StringBuffer sql = this.getUpdateSql(this.tablename, mustmap, jsons, paramslist, "userid");
		return this.responseMessage(this.jdbcTemplate.update(sql.toString(), paramslist.toArray()),null);
	}
	
	@RequestMapping(value = "/delUserInfo",method = RequestMethod.POST)
	public Map<String, Object>  delUserInfo(String json) throws JSONException {
		JSONObject jsons = new JSONObject(json);
		List <Object> paramslist = new  ArrayList<Object>();  
		StringBuffer sql = this.getDelSql(this.tablename, jsons, paramslist, "userid");
		return this.responseMessage(this.jdbcTemplate.update(sql.toString(), paramslist.toArray()),null);
	}
	
	

	

}
