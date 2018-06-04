package com.kayak.customer;


import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.UserFeignClient;


@RestController
public class UserFeignApi {
	
	@Resource
	private UserFeignClient userClient;
	
	@RequestMapping("/login")
	public Map<String, Object> login(String userloginnname, String userpwd) {
		return this.userClient.getUserInfoByNamePwd(userloginnname, userpwd);
	}

}
