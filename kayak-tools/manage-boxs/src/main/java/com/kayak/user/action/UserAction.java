package com.kayak.user.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.sql.SqlRow;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.util.Tools;
import com.kayak.user.dao.UserDao;

@RestController
public class UserAction extends BaseController{
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/user/updateMyPassword.json")
	public String updateMyPassword() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			String user_name = Tools.obj2Str(parmas.get("username"));
			String user_password = Tools.obj2Str(parmas.get("userpassword"));
			String new_password =Tools.obj2Str(parmas.get("newpassword"));
			SqlRow user = this.userDao.findUser(user_name);
			if (!user.getString("user_password").equals(user_password)) {
				return super.updateFailure("账号或者密码错误");
			}
			return this.updateSuccess(this.userDao.updateUserPasswd(user.getString("user_id"), new_password) + ",下次登陆时生效!");
		} catch (Exception e) {
			return this.updateFailure(e.getMessage());
		}
	}

}
