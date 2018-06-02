package com.kayak.base.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * 用于JdbcTemplate查询的时候构造SqlResult对象
 */
public class KResultSetExtractor implements ResultSetExtractor<SqlResult> {

	@Override
	public SqlResult extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		return new SqlResult(rs);
	}

}
