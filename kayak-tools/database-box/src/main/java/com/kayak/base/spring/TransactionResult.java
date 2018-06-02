package com.kayak.base.spring;

import com.kayak.base.exception.KPromptException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.TResult;

public class TransactionResult {
	private Exception exception;
	private String returnmsg;
	private SqlResult sqlResult;
	private TResult tResult;

	public TResult gettResult() {
		return tResult;
	}

	public void settResult(TResult tResult) {
		this.tResult = tResult;
	}

	public TransactionResult() {
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public void throwException() throws KPromptException {
		if (this.exception != null) {
			if (this.exception instanceof KPromptException) {
				throw (KPromptException) this.exception;
			} else {
				throw new KPromptException(this.exception.getMessage());
			}
		}
	}

	public String getReturnmsg() {
		return returnmsg;
	}

	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}

	public SqlResult getSqlResult() {
		return sqlResult;
	}

	public void setSqlResult(SqlResult sqlResult) {
		this.sqlResult = sqlResult;
	}

}
