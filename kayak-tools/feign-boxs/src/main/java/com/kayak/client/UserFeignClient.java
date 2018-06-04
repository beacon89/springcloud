package com.kayak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kayak.user.UserInfoObj;

@FeignClient(name="kayak-system")
public interface UserFeignClient {

	@RequestMapping(value = "/getUserInfoByNamePwd")
	public @ResponseBody UserInfoObj getUserInfoByNamePwd(@RequestParam("username")String username, @RequestParam("userpwd")String userpwd);
	
}
