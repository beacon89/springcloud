package com.kayak.client;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "kayak-system")
public interface UserFeignClient {

	@RequestMapping(value = "/getUserInfoByNamePwd")
	public @ResponseBody Map<String, Object> getUserInfoByNamePwd(String userloginnname, String userpwd);

	@RequestMapping(value = "/getUserInfoById")
	public Map<String, Object> getUserInfoById(String userid);

	@RequestMapping(value = "/getUserInfoList")
	public List<Map<String, Object>> getUserInfoList(String userloginnname, String username, String userdept,
			String userstatus, String usermobileno, int pageNumber, int pageSize);
	
	@RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
	public int saveUserInfo(String json) throws JSONException;
	
	@RequestMapping(value = "/UpdataUserInfo", method = RequestMethod.POST)
	public int UpdataUserInfo(String json) throws JSONException;
	
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public int changeStatus(String userid);

}
