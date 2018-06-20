package com.kayak.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;

@Repository
public class UserDao extends ComnDao {

	/**
	 * 查询用户
	 * 
	 * @param user_name
	 * @return
	 * @throws Exception
	 */
	public SqlRow findUser(String user_name) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		SqlResult sr = super.doQueryBySqlid("find_sys_user_by_name", params);
		List<SqlRow> sqlRows = sr.getRows();

		if (sqlRows != null && sqlRows.size() > 0) {
			return sqlRows.get(0);
		}

		return null;
	}

}
