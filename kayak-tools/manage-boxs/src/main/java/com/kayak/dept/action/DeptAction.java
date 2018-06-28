package com.kayak.dept.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.base.action.BaseController;
import com.kayak.base.sql.SqlRow;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.util.Tools;
import com.kayak.dept.dao.DeptDao;
import com.kayak.role.dao.RoleDao;

@RestController
public class DeptAction extends BaseController{
	
	@Autowired
	private DeptDao deptDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping(value = "/dept/finddeptroles.json")
	public String findDeptRoles() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			int dept_id = Tools.str2Int(Tools.obj2Str(parmas.get("dept_id")));
			//获取当前已经设定的角色
			List<SqlRow> deptrolelist = this.deptDao.findSysDeptRoles(dept_id);
			Set<String> deptroleset = new HashSet<>();
			for(SqlRow row:deptrolelist) {
				deptroleset.add(row.getString("role_id"));
			}
			//获取所有角色
			List<SqlRow> rolelist = roleDao.findSysRoles();
			for(SqlRow row:rolelist) {
				if(deptroleset.contains(row.getString("role_id"))) {
					row.put("role_check", true);
					continue;
				}
				row.put("role_check", false);
			}
			Map<String, Object> returnmap = new HashMap<>();
			returnmap.put("rows", rolelist);
			return this.returnUpdate(true, "查询成功", returnmap);
		} catch (Exception e) {
			return this.updateFailure(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dept/updatedeptroles.json")
	public String updatedeptroles() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			int dept_id = Tools.str2Int(Tools.obj2Str(parmas.get("dept_id")));
			List<Integer> os = (List<Integer>) parmas.get("roleidarray");
			String returnmessage = roleDao.addsysRoles(dept_id, os);
			System.out.println(returnmessage);
			return this.updateSuccess("更新成功!");
		} catch (Exception e) {
			return this.updateFailure(e.getMessage());
		}
	}

}
