package com.kayak.dao;

import com.kayak.user.UserInfoObj;

public interface UserInfoDao {

	UserInfoObj getUserInfoByNamePwd(String username,String userpwd);
}
