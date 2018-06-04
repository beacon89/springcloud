package com.kayak.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.dao.imp.UserInfoDaoImp;
import com.kayak.user.UserInfoObj;

/**
 * 用户
 * @author beacon
 *
 */
@RestController
public class UserInfoController {

	@Autowired
	private UserInfoDaoImp userInfoImp;
	
	@RequestMapping(value="/getUserInfoByNamePwd")
	public @ResponseBody UserInfoObj getUserInfoByNamePwd(String username, String userpwd) {
		return userInfoImp.getUserInfoByNamePwd(username, userpwd);
	}
	
}
