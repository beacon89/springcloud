package com.kayak.base.sql.object;

public class PreparedParam {
	@SuppressWarnings("rawtypes")
	private Class type;
	private Object value;

	public PreparedParam(@SuppressWarnings("rawtypes") Class type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	@SuppressWarnings("rawtypes")
	public Class getType() {
		return type;
	}

	public void setType(@SuppressWarnings("rawtypes") Class type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
