package com.kayak.base.sql.object;

import java.util.ArrayList;
import java.util.List;

import com.kayak.base.dynamicds.CustomerContextHolder;
import com.kayak.base.system.SysBeans;

/**
 * 通用SQL配置模型
 */
public class SqlConfig {
	class Sql {
		String sql;
		String dialect;

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			if (sql == null) {
				this.sql = "";
			} else {
				this.sql = sql.trim();
			}
		}

		public String getDialect() {
			return dialect;
		}

		public void setDialect(String dialect) {
			this.dialect = dialect;
		}
	}

	private List<Sql> sqls = new ArrayList<Sql>();
	private String sqlid;
	private String sqlname;
	// private String sql;
	private List<SqlCheck> checks;
	private List<SqlTransfer> transfers;
	private SqlQueryTree qureytree;

	// private String dialect;

	public SqlConfig(String sqlid, String sqlname, List<SqlCheck> checks,
			List<SqlTransfer> transfers, SqlQueryTree qureytree) {
		this.sqlid = sqlid;
		this.sqlname = sqlname;

		this.checks = checks;
		this.transfers = transfers;
		this.qureytree = qureytree;

	}

	public SqlConfig(String sqlname, List<SqlCheck> checks,
			List<SqlTransfer> transfers, SqlQueryTree qureytree) {
		this.sqlname = sqlname;

		this.checks = checks;
		this.transfers = transfers;
		this.qureytree = qureytree;

	}

	public List<Sql> getSqls() {
		return sqls;
	}

	public void setSqls(List<Sql> sqls) {
		this.sqls = sqls;
	}

	public String getSqlid() {
		return sqlid;
	}

	public void setSqlid(String sqlid) {
		this.sqlid = sqlid;
	}

	public String getSqlname() {
		return sqlname;
	}

	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}

	// public String getSql()
	// {
	// return sql;
	// }

	public List<SqlCheck> getChecks() {
		return checks;
	}

	public void setChecks(List<SqlCheck> checks) {
		this.checks = checks;
	}

	public List<SqlTransfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<SqlTransfer> transfers) {
		this.transfers = transfers;
	}

	public SqlQueryTree getQureytree() {
		return qureytree;
	}

	public void setQureytree(SqlQueryTree qureytree) {
		this.qureytree = qureytree;
	}

	public String getSql(String dialect) {
		if (sqls == null && sqls.size() < 1) {
			return "";
		}
		if (dialect != null)
			for (Sql s : sqls) {
				if (dialect.equals(s.getDialect())) {
					return s.getSql();
				}
			}
		return sqls.get(0).getSql();

	}

	public String getSql() {
		String dbid = CustomerContextHolder.getCustomerType();
		String dbtype = SysBeans.getSysSql().getDBType(dbid);
		if (dbtype == null) {
			dbtype = "";
		}
		return getSql(dbtype);

	}

	public void addSql(String sql, String dialect) {
		Sql s = new Sql();
		s.setSql(sql);
		s.setDialect(dialect);
		sqls.add(s);
	}
}
