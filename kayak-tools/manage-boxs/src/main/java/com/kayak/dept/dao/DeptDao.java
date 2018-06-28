package com.kayak.dept.dao;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.exception.KSystemException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;

@Repository
public class DeptDao extends ComnDao{

	
	/**
	 * 获取该部门所设定的所有角色
	 * @param dept_id
	 * @return
	 * @throws Exception 
	 */
	public List<SqlRow> findSysDeptRoles(int dept_id) throws Exception{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dept_id", dept_id);
			SqlResult sr = super.doQueryBySqlid("find_sys_dept_role", params);
			return sr.getRows();
		} catch (KPromptException | KSystemException | SQLException | KSqlException e) {
			throw e;
		}
	}
	
	public SqlRow findSysDeptById(int dept_id) throws Exception{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dept_id", dept_id);
			SqlResult sr = this.query("find_sys_dept_byid", params);
			if(!sr.getRow().isEmpty()) {
				return sr.getRows().get(0);
			}
			return null;
		} catch (KPromptException | KSqlException e) {
			throw e;
		}
	}
	
	
	
}
