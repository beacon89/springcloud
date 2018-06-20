package com.kayak.base.sql;

/**
 * 用于ITransaction.transaction方法指定返回的对象类型
 */
public class TResult
{
	private boolean success;

	private SqlResult sresult;

	private Integer fetrows;

	private Object object;

	/**
	 * 用于ITransaction.transaction方法指定返回的对象类型
	 * 
	 * @param success
	 *            此属性必须设置，事务逻辑执行结果，事务将根据这个success：<br />
	 *            当success=true则事务正常提交，当success= false则事务回滚后返回
	 * @param sresult
	 *            要返回的查询结果集
	 * @param fetrows
	 *            要返回的更新影响记录数
	 * @param object
	 *            其它要返回的对象
	 */
	public TResult(boolean success, SqlResult sresult, Integer fetrows, Object object)
	{
		this.success = success;
		this.sresult = sresult;
		this.fetrows = fetrows;
		this.object = object;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public SqlResult getSresult()
	{
		return sresult;
	}

	public void setSresult(SqlResult sresult)
	{
		this.sresult = sresult;
	}

	public Integer getFetrows()
	{
		return fetrows;
	}

	public void setFetrows(Integer fetrows)
	{
		this.fetrows = fetrows;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}

}
