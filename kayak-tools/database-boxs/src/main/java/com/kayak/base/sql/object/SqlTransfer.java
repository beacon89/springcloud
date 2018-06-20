package com.kayak.base.sql.object;

public class SqlTransfer {
	private String column;
	private String target;
	private String dict;

	public SqlTransfer(String column, String target, String dict) {
		this.column = column;
		this.target = target;
		this.dict = dict;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

}
