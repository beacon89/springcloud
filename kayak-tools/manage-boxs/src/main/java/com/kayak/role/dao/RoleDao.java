package com.kayak.role.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;

@Repository
public class RoleDao extends ComnDao{

	/**
	 * 查询数字字典子项列表
	 * 
	 * @param dict
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findSysRoles() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		SqlResult sr = super.doQueryBySqlid("find_sys_roles", params);
		return sr.getRows();
	}
	
	public String addsysRoles(int dept_id,List<Integer> os) throws Exception {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Integer[] dept_ids = new Integer[os.size()];
			for(int i = 0;i<dept_ids.length;i++) {
				dept_ids[i] = dept_id;
			}
			params.put("dept_id", dept_id);
			params.put("dept_ids", dept_ids);
			params.put("role_ids", os.toArray(new Integer[] {}));
			return this.update("add_sys_dept_role", params);
		} catch (KPromptException | KSqlException e) {
			throw e;
		}
	}
	
	
	
}
