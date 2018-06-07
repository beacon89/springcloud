package com.kayak.client;

import java.util.Map;

import org.json.JSONException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kayak.remote.UserFeignApiHystrix;

@FeignClient(name = "kayak-system",fallback = UserFeignApiHystrix.class)
public interface UserFeignClient {
	
	@RequestMapping(value = "/getUserInfoList",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoList(@RequestParam("json") final String json, @RequestParam("pageNumber") final int pageNumber, @RequestParam("pageSize") final int pageSize) throws JSONException;
	
	@RequestMapping(value = "/getUserInfoTotalCount",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoTotalCount();
	
	@RequestMapping(value = "/checkUserInfoLogname",method = RequestMethod.POST)
	public Map<String, Object> getCheckUserInfo(@RequestParam("json") final String json) throws JSONException;
	
	@RequestMapping(value = "/UpdataUserInfo",method = RequestMethod.POST)
	public Map<String, Object> UpdataUserInfo(@RequestParam("json") final String json) throws JSONException;
	
	@RequestMapping(value = "/getUserInfoByNamePwd",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoByNamePwd(@RequestParam("json") final String json) throws JSONException;

	@RequestMapping(value = "/getUserInfoById",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoById(@RequestParam("json") final String json) throws JSONException;
	
	@RequestMapping(value = "/saveUserInfo",method = RequestMethod.POST)
	public Map<String, Object> saveUserInfo(@RequestParam("json") final String json) throws JSONException;
	
	@RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
	public Map<String, Object> changeStatus(@RequestParam("json") final String json) throws JSONException;
	
	@RequestMapping(value = "/delUserInfo",method = RequestMethod.POST)
	public Map<String, Object>  delUserInfo(@RequestParam("json") final String json) throws JSONException;
	
	

	


	
}
