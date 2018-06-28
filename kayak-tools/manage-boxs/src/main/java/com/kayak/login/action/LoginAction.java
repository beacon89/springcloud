package com.kayak.login.action;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.sql.SqlRow;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.system.SysUtil;
import com.kayak.base.util.Tools;
import com.kayak.user.dao.UserDao;
import com.kayak.util.JwtUtil;
import com.kayak.util.StaticVariable;

@RestController
public class LoginAction extends BaseController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/login.json")
	public String login() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();

			String username = Tools.obj2Str(parmas.get("username"));
			String password = Tools.obj2Str(parmas.get("password"));

			SqlRow user = userDao.findUser(username);

			if (user == null) {
				return super.updateFailure("账号或者密码错误");
			}

			if (!user.getString("user_password").equals(password)) {
				return super.updateFailure("账号或者密码错误");
			}
			if (!user.getString("user_status").equals(StaticVariable.SYS_USER_STATUS1)) {
				return super.updateFailure("用户状态异常，请联系管理员!");
			}
			Map<String, Object> returnmap = new HashMap<>();
			String token = JwtUtil.getToken(user.getString("user_id"));
			returnmap.put("token", token);
			returnmap.put("deptname", user.getString("dept_name"));
			returnmap.put("deptid", user.getString("dept_id"));
			returnmap.put("roleid", user.getString("role_id"));
			returnmap.put("roletype", user.getString("role_type"));
			return super.updateSuccess(returnmap);
		} catch (Exception e) {
			return super.updateFailure("服务器异常，请稍后服务");
		}
	}

	@RequestMapping(value = "/freshToken.json")
	public String freshToken() {
		JSONObject json = new JSONObject();
		try {
			String user_id = Tools.obj2Str(SysUtil.getSysUserParamValue("userid"));
			String token = JwtUtil.getToken(user_id);
			json.put("token", token);
			json.put("success", true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return super.updateFailure("服务器异常，请稍后服务");
		}
		return json.toString();
	}

}
