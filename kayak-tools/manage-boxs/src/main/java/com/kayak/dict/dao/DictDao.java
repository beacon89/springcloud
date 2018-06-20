package com.kayak.dict.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;

@Repository
public class DictDao extends ComnDao {

	/**
	 * 查询数字字典子项列表
	 * 
	 * @param dict
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findDictItems(String dict) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dict", dict);
		SqlResult sr = super.doQueryBySqlid("find_sys_dict_items", params);
		return sr.getRows();
	}

}
