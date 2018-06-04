package com.kayak.dao.imp;

import org.springframework.stereotype.Repository;

import com.kayak.dao.UserInfoDao;
import com.kayak.user.UserInfoObj;

@Repository
public class UserInfoDaoImp implements UserInfoDao{

//	@Autowired
//    private JdbcTemplate jdbcTemplate;
	
	@Override
	public UserInfoObj getUserInfoByNamePwd(String username, String userpwd) {
		UserInfoObj o = new UserInfoObj();
		o.setUserName("1");
		o.setUserPwd("2");
		return o;
	}


}
