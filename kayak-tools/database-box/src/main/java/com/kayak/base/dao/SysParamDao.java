package com.kayak.base.dao;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.kayak.base.exception.KSqlException;
import com.kayak.base.exception.KSystemException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlStatement;
import com.kayak.base.sql.SqlUtil;

@Repository
public class SysParamDao extends BaseDao {

	public Object getParaValue(String sql, String dataSource,
			final Map<String, Object> params) throws KSystemException,
			SQLException, KSqlException, KSqlException {

		SqlUtil.selectDataSource(dataSource);

		SqlStatement stm = new SqlStatement(sql, getJdbcTemplate());

		stm.autoSetParams(params, false);

		SqlResult sr = stm.executeQuery();

		if (sr != null && sr.next()) {
			return sr.getByColumnIndex(0);// 只取查询结果的第一行第一列的值
		}

		return null;
	}

}
