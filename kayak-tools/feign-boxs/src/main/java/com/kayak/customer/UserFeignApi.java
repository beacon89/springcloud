package com.kayak.customer;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.UserFeignClient;
import com.kayak.user.UserInfoObj;


@RestController
public class UserFeignApi {
	
	@Resource
	private UserFeignClient userClient;
	
	@RequestMapping("/login")
	public UserInfoObj login() {
		return this.userClient.getUserInfoByNamePwd("1111","2222");
	}

}
