package com.kayak.base.sql;

import java.sql.SQLException;
import java.util.Map;

import com.kayak.base.exception.KSqlException;

public interface ISqlStatement {

	public void autoSetParams(Map<String, Object> params) throws KSqlException,
			KSqlException;

	public SqlResult executeQuery() throws KSqlException, SQLException;

	public void autoSetParams(final Map<String, Object> params, boolean paging)
			throws KSqlException, KSqlException;
	
	public int executeUpdate() throws KSqlException, SQLException;

}
