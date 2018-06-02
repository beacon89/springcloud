package com.kayak.base.sql.object;

import java.util.ArrayList;
import java.util.List;

public class SqlCheck
{

	private class CheckSql
	{
		private String checksql;
		private String dialect;

		public String getChecksql()
		{
			return checksql;
		}

		public void setChecksql(String checksql)
		{
			this.checksql = checksql;
		}

		public String getDialect()
		{
			return dialect;
		}

		public void setDialect(String dialect)
		{
			this.dialect = dialect;
		}
	}

	private String checkname;
	private String checksql;
	private String checksqlid;
	private String checkstring;
	private String compareval;
	private String comparesign;
	private String errtext;
	private Boolean exitall;

	private List<CheckSql> checkSqls = new ArrayList<CheckSql>();

	public SqlCheck(String checkname, String checksql, String checksqlid, String checkstring,
			String compareval, String comparesign, String errtext, Boolean exitall)
	{
		this.checkname = checkname;
		this.checksql = checksql;
		this.checksqlid = checksqlid;
		this.checkstring = checkstring;
		this.compareval = compareval;
		this.comparesign = comparesign;
		this.errtext = errtext;
		this.exitall = exitall;
	}

	public SqlCheck(String checkname, String checksql, String checkstring,
			String compareval, String comparesign, String errtext,
			Boolean exitall) {
		this.checkname = checkname;
		this.checksql = checksql;
		this.checkstring = checkstring;
		this.compareval = compareval;
		this.comparesign = comparesign;
		this.errtext = errtext;
		this.exitall = exitall;
	}

	public String getCheckname() {
		return checkname;
	}

	public void setCheckname(String checkname)
	{
		this.checkname = checkname;
	}

	public String getChecksql()
	{
		return checksql;
	}

	public void setChecksql(String checksql)
	{
		this.checksql = checksql;
	}

	public String getChecksqlid()
	{
		return checksqlid;
	}

	public void setChecksqlid(String checksqlid)
	{
		this.checksqlid = checksqlid;
	}

	public String getCheckstring()
	{
		return checkstring;
	}

	public void setCheckstring(String checkstring)
	{
		this.checkstring = checkstring;
	}

	public String getCompareval()
	{
		return compareval;
	}

	public void setCompareval(String compareval)
	{
		this.compareval = compareval;
	}

	public String getComparesign()
	{
		return comparesign;
	}

	public void setComparesign(String comparesign)
	{
		this.comparesign = comparesign;
	}

	public String getErrtext()
	{
		return errtext;
	}

	public void setErrtext(String errtext)
	{
		this.errtext = errtext;
	}

	public Boolean getExitall()
	{
		return exitall;
	}

	public void setExitall(Boolean exitall)
	{
		this.exitall = exitall;
	}

	public void addCheckSql(String sql, String dialect)
	{
		CheckSql s = new CheckSql();
		s.setChecksql(sql);
		s.setDialect(dialect);
		checkSqls.add(s);
	}

	/**
	 * 获取指定方言对应的checksql
	 * 
	 * @param dialect
	 * @return
	 */
	public String getChecksql(String dialect)
	{
		if (checkSqls == null || checkSqls.size() < 1)
		{
			return "";
		}
		for (CheckSql s : checkSqls)
		{
			if (dialect.equals(s.getDialect()))
			{
				return s.getChecksql();
			}
		}
		return checkSqls.get(0).getChecksql();
	}

}
