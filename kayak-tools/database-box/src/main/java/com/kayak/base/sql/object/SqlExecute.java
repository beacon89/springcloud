package com.kayak.base.sql.object;

import java.util.List;

public class SqlExecute
{
	private String exeid;

	private String exename;

	private String logexeid;

	private String datasource;

	private List<String> sqlids;
	
	private boolean pstm;

	private List<SqlConfig> sqlinfos;

	public SqlExecute(String exeid, String exename, String logexeid,
			String datasource, List<String> sqlids, List<SqlConfig> sqlinfos) {
		super();
		this.exeid = exeid;
		this.exename = exename;
		this.logexeid = logexeid;
		this.datasource = datasource;
		this.sqlids = sqlids;
		this.sqlinfos = sqlinfos;
	}

	public boolean isPstm() {
		return pstm;
	}

	public void setPstm(boolean pstm) {
		this.pstm = pstm;
	}

	public String getExeid()
	{
		return exeid;
	}

	public void setExeid(String exeid)
	{
		this.exeid = exeid;
	}

	public String getExename()
	{
		return exename;
	}

	public void setExename(String exename)
	{
		this.exename = exename;
	}

	public String getLogexeid()
	{
		return logexeid;
	}

	public void setLogexeid(String logexeid)
	{
		this.logexeid = logexeid;
	}

	public String getDatasource()
	{
		return datasource;
	}

	public void setDatasource(String datasource)
	{
		this.datasource = datasource;
	}

	public List<String> getSqlids()
	{
		return sqlids;
	}

	public void setSqlids(List<String> sqlids)
	{
		this.sqlids = sqlids;
	}

	public List<SqlConfig> getSqlinfos() {
		return sqlinfos;
	}

	public void setSqlinfos(List<SqlConfig> sqlinfos) {
		this.sqlinfos = sqlinfos;
	}

}
