package com.kayak.params.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;

@Repository
public class ParamsDao extends ComnDao {

	/**
	 * 查询系统参数
	 * 
	 * @param param_key
	 * @return
	 * @throws Exception
	 */
	public SqlRow findDictItems(String param_key) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param_key", param_key);
		SqlResult sr = super.doQueryBySqlid("find_sys_param", params);
		List<SqlRow> sqlRows = sr.getRows();

		if (sqlRows != null && sqlRows.size() > 0) {
			return sqlRows.get(0);
		}

		return null;
	}

}
