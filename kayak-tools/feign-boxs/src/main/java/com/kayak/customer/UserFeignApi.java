package com.kayak.customer;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.UserFeignClient;

@RestController
public class UserFeignApi {

	@Resource
	private UserFeignClient userClient;

	//OK～
	@RequestMapping(value = "/getUserInfoList",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoList(String json, int pageNumber, int pageSize){
		return this.userClient.getUserInfoList(json, pageNumber, pageSize);
	}
	
	@RequestMapping(value = "/getUserInfoTotalCount",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoTotalCount() {
		return this.userClient.getUserInfoTotalCount();
	}
	
	//OK
	@RequestMapping(value = "/UpdataUserInfo",method = RequestMethod.POST)
	public Map<String, Object> UpdataUserInfo(String json) {
		return this.userClient.UpdataUserInfo(json);
	}
	
	@RequestMapping(value = "/getUserInfoByNamePwd",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoByNamePwd(String json){
		return this.userClient.getUserInfoByNamePwd(json);
	}
	
	@RequestMapping(value = "/getUserInfoById",method = RequestMethod.POST)
	public Map<String, Object> getUserInfoById(String json){
		return this.userClient.getUserInfoById(json);
	}
	
	//OK～
	@RequestMapping(value = "/saveUserInfo",method = RequestMethod.POST)
	public Map<String, Object> saveUserInfo(String json) {
		return this.userClient.saveUserInfo(json);
	}
	
	//OK
	@RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
	public Map<String, Object> changeStatus(String json) {
		return this.userClient.changeStatus(json);
	}
	
	//OK
	@RequestMapping(value = "/delUserInfo",method = RequestMethod.POST)
	public Map<String, Object>  delUserInfo(String json) {
		return this.userClient.delUserInfo(json);
	}
	
	//OK
	@RequestMapping(value = "/checkUserInfoLogname",method = RequestMethod.POST)
	public Map<String, Object> getCheckUserInfo(String json) {
		return this.userClient.getCheckUserInfo(json);
	}

}
