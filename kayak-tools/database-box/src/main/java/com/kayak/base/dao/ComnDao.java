package com.kayak.base.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import com.kayak.base.dynamicds.CustomerContextHolder;
import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.exception.KSystemException;
import com.kayak.base.sql.ISqlStatement;
import com.kayak.base.sql.ITransaction;
import com.kayak.base.sql.PreparedSqlStatement;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlStatement;
import com.kayak.base.sql.SqlUtil;
import com.kayak.base.sql.SysSql;
import com.kayak.base.sql.TResult;
import com.kayak.base.sql.object.SqlCheck;
import com.kayak.base.sql.object.SqlConfig;
import com.kayak.base.sql.object.SqlExecute;
import com.kayak.base.system.RequestSupportDb;
import com.kayak.base.system.SysBeans;
import com.kayak.base.util.Tools;

@Repository
public class ComnDao extends BaseDao {
	private static Logger log = Logger.getLogger(ComnDao.class);

	@Resource
	private TransactionTemplate transactionTemplate;

	/**
	 * 获取当前执行的SQL语句对应的数据库类型
	 */
	public String getDBType() {

		String dbid = CustomerContextHolder.getCustomerType();
		String dbtype = SysBeans.getSysSql().getDBType(dbid);

		return dbtype;
	}

	/**
	 * 执行查询并返回SqlResult结果集<br />
	 * <strong>如果SQL语句中有参数需要注入，需要在调用此方法前使用ComnDao对象的setParams方法设置参数，
	 * 并且把exeid添加在params中， 或者使用query(String, Map<String,Object>)方法</strong>
	 * 
	 * @return 查询结果集，出错返回null
	 * @throws KSqlException
	 * @throws KSystemException
	 * @throws KSqlException
	 * @throws SQLException
	 */
	public SqlResult query(Map<String, Object> params) throws KPromptException, KSqlException {
		return query(null, params, null, null);
	}

	/**
	 * 执行查询并返回SqlResult结果集<br />
	 * <strong>如果SQL语句中有参数需要注入，需要在调用此方法前使用ComnDao对象的setParams方法设置参数，
	 * 并且把exeid添加在params中， 或者使用query(String, Map<String,Object>)方法</strong>
	 * 
	 * @return 查询结果集，出错返回null
	 * @throws KSqlException
	 * @throws KSystemException
	 * @throws KSqlException
	 * @throws SQLException
	 */
	public SqlResult query(String exeid, Map<String, Object> params) throws KPromptException, KSqlException {
		return query(exeid, params, null, null);
	}

	/**
	 * 执行查询并返回SqlResult结果集<br />
	 * 在执行查询前，必须给params成员设置值<br />
	 * 通用查询只支持指定exeid
	 * 
	 * @param exeid
	 *            如果指定exeid则执行exeid指定的通用SQL，否则尝试从params中取得exeid参数来使用
	 * @param start
	 *            起始记录，如果传null或小于零的值，则忽略该参数
	 * @param limit
	 *            最大记录数，如果传null或小于1的值，则忽略该参数
	 * @return 查询结果集，出错返回null
	 * @throws KSqlException
	 * @throws KPromptException
	 */
	public SqlResult query(String exeid, Map<String, Object> params, Integer start, Integer limit)
			throws KPromptException, KSqlException {
		if (params == null)
			params = new HashMap<String, Object>();

		if (exeid == null)
			exeid = Tools.trimString(params.get("exeid"));
		else
			params.put("exeid", exeid);

		// 取得exe配置
		final SqlExecute execonfig = SysSql.getExeid(exeid);
		if (execonfig == null)
			throw new KSqlException("找不到 exeid : " + exeid);

		TResult tr = doTransaction(new ITransaction() {
			@Override
			public TResult transaction(JdbcTemplate jdbcTemplate, Map<String, Object> params)
					throws KPromptException, Exception {

				SqlResult result = exeQuery(execonfig, params);

				return new TResult(true, result, null, null);
			}
		}, execonfig.getDatasource(), params);

		return tr.getSresult();
	}

	/**
	 * 嵌套于事务的执行sql配置查询
	 * 
	 * @param execonfig
	 * @param params
	 * @return
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	public SqlResult exeQuery(String exeid, Map<String, Object> params)
			throws KPromptException, KSqlException, KSystemException, SQLException {
		// 取得exe配置
		final SqlExecute execonfig = SysSql.getExeid(exeid);
		if (execonfig == null)
			throw new KSqlException("找不到 exeid : " + exeid);
		return exeQuery(execonfig, params);
	}

	/**
	 * 嵌套于事务的执行sql配置查询
	 * 
	 * @param execonfig
	 * @param params
	 * @return
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	private SqlResult exeQuery(final SqlExecute execonfig, final Map<String, Object> params)
			throws KPromptException, KSqlException, KSystemException, SQLException {
		String dbtype = getDBType();

		boolean dolog = !params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG);

		@SuppressWarnings("rawtypes")
		List sqllist = execonfig.getSqlinfos();
		if (sqllist == null || sqllist.size() == 0) {
			sqllist = execonfig.getSqlids();
		}
		SqlResult sr = null;// 保存最后一个查询执行结果用于返回
		for (Object sqlobj : sqllist) {// 遍历所有同步执行的SQL语句
			SqlConfig sqlconfig;
			if (sqlobj instanceof String) {
				sqlconfig = SysSql.getSqlid((String) sqlobj, dbtype);
				// 当前执行的sqlid
				String exeSqlid = sqlconfig.getSqlid();
				if (dolog) {
					log.info("##### comn query exeid : " + execonfig.getExeid() + ", execute sqlid : " + exeSqlid + ", "
							+ dbtype);
				}
			} else {
				sqlconfig = (SqlConfig) sqlobj;
				if (dolog) {
					log.info("##### comn query exeid : " + execonfig.getExeid() + ", execute sqlname : "
							+ sqlconfig.getSqlname() + ", " + dbtype);
				}
			}

			String sql_statement = sqlconfig.getSql(dbtype);
			// 准备更新执行
			ISqlStatement stmExecute;
			if (SqlUtil.isPrepared) {
				stmExecute = new PreparedSqlStatement(sql_statement, getJdbcTemplate());
			} else {
				stmExecute = new SqlStatement(sql_statement, getJdbcTemplate());
			}

			// 进行SQL执行前校验
			if (sqlCheck(getJdbcTemplate(), sqlconfig, params)) {// SQL校验成功
				String check_sql = sql_statement;
				check_sql = check_sql.replaceAll("\\(", "").trim();

				if ("SELECT".equalsIgnoreCase(check_sql.substring(0, 6))
						|| "WITH".equalsIgnoreCase(check_sql.substring(0, 4))) {
					stmExecute.autoSetParams(params);
					// 执行查询语句
					sr = stmExecute.executeQuery();
					sr.setQueryTree(sqlconfig.getQureytree());
					sr.setTransfers(sqlconfig.getTransfers());
				} else {
					// 设置参数，但不设置分页参数，
					stmExecute.autoSetParams(params, true);
					// 执行更新语句
					stmExecute.executeUpdate();
				}

				// if ("SELECT".equalsIgnoreCase(sql_statement.substring(0, 6)))
				// {
				// stmExecute.autoSetParams(params);
				// // 执行查询语句
				// sr = stmExecute.executeQuery();
				// sr.setQueryTree(sqlconfig.getQureytree());
				// sr.setTransfers(sqlconfig.getTransfers());
				// } else {
				// // 设置参数，但不设置分页参数，
				// stmExecute.autoSetParams(params, true);
				// // 执行更新语句
				// stmExecute.executeUpdate();
				// }
			}
		} // end for
		if (sr == null) {
			throw new KSqlException("执行查询操作里的SQL没有SELECT语句exeid=" + execonfig.getExeid());
		}
		return sr;
	}

	/**
	 * 执行通用SQL更新<br />
	 * <strong>如果SQL语句中有参数需要注入，需要在调用此方法前使用ComnDao对象的setParams方法设置参数，
	 * 并且把exeid添加在params中， 或者使用update(String, Map<String,Object>)方法</strong>
	 * 
	 * @return 返回处理结果信息，出错返回null
	 * @throws KSqlException
	 * @throws KPromptException
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	public String update(final Map<String, Object> params) throws KPromptException, KSqlException {
		return update(null, params);
	}

	/**
	 * 执行通用SQL更新<br />
	 * 在执行更新前，必须给params成员设置值<br />
	 * 通用查询只支持指定exeid
	 * 
	 * @param exeid
	 *            如果指定exeid则执行exeid指定的通用SQL，否则尝试从params中取得exeid参数来使用
	 * @return 返回处理结果信息，出错返回null
	 * @throws KPromptException
	 */
	public String update(String exeid, final Map<String, Object> params) throws KPromptException, KSqlException {
		if (params != null) {
			if (exeid == null)
				exeid = Tools.trimString(params.get("exeid"));
			else
				params.put("exeid", exeid);
		}
		return doUpdate(exeid, params);
	}

	/**
	 * 执行通用SQL更新<br />
	 * 在执行更新前，必须给params成员设置值<br />
	 * 通用查询只支持指定exeid
	 * 
	 * @param exeid
	 *            如果指定exeid则执行exeid指定的通用SQL，否则尝试从params中取得exeid参数来使用
	 * @param dolog
	 *            是否需要由dao记录日志。如果不需要记录日志，则此处传入false
	 * @return 返回处理结果信息，出错返回null
	 */
	public String update(String exeid, final Map<String, Object> params, boolean dolog)
			throws KPromptException, KSqlException {
		if (params != null) {
			if (exeid == null)
				exeid = Tools.trimString(params.get("exeid"));
			else
				params.put("exeid", exeid);
		}
		return doUpdate(exeid, params, dolog);
	}

	private String doUpdate(String exeid, final Map<String, Object> params) throws KPromptException, KSqlException {
		return doUpdate(exeid, params, true);
	}

	/**
	 * 执行通用SQL更新
	 * 
	 * @param exeid
	 *            如果指定exeid则执行exeid指定的通用SQL，否则尝试从params中取得exeid参数来使用
	 * @return 返回处理结果信息，出错返回null
	 * @throws KPromptException
	 *             在SQL校验检查失败时，抛出相应的错误信息，在调用此方法时，必须处理这个错误信息（如返回提示给用户）
	 */
	private String doUpdate(String exeid, final Map<String, Object> params, final boolean dolog)
			throws KPromptException, KSqlException {
		// 读取SQL配置
		final SqlExecute execonfig = SysSql.getExeid(exeid);
		if (execonfig == null) {
			throw new KSqlException(String.format("找不到exeid:%s", exeid));
		}

		TResult tr = doTransaction(new ITransaction() {
			@Override
			public TResult transaction(JdbcTemplate jdbcTemplate, Map<String, Object> params)
					throws KPromptException, Exception {
				String result = exeUpdate(execonfig, params, dolog);
				return new TResult(true, null, null, result);
			}
		}, execonfig.getDatasource(), params);

		return (String) tr.getObject();

	}

	public String exeUpdate(final String exeid, final Map<String, Object> params)
			throws KPromptException, KSqlException, SQLException, KSystemException {
		// 读取SQL配置
		final SqlExecute execonfig = SysSql.getExeid(exeid);
		if (execonfig == null) {
			throw new KSqlException(String.format("找不到exeid:%s", exeid));
		}
		return exeUpdate(execonfig, params, true);
	}

	private String exeUpdate(final SqlExecute execonfig, final Map<String, Object> params, boolean dolog)
			throws KPromptException, KSqlException, SQLException, KSqlException, KSystemException {
		String dbtype = getDBType();

		String descript = null;// 操作描述，从按钮配置中取
		if (descript == null) {// 如果前面从按钮配置找不到操作描述内容，则尝试从SQL执行配置中取EXENAME
			descript = execonfig.getExename();
		}

		// 收集执行完成的sqlid，以便在提交事务后，供缓存管理器更新缓存
		Set<String> executedSqlids = new HashSet<String>();
		// 收集执行更新的返回值（影响的行数）
		List<Integer> fetchRows = new ArrayList<Integer>();

		@SuppressWarnings("rawtypes")
		List sqllist = execonfig.getSqlinfos();
		if (sqllist == null || sqllist.size() == 0) {
			sqllist = execonfig.getSqlids();
		}
		for (Object sqlobj : sqllist) {// 遍历所有同步执行的SQL语句
			SqlConfig sqlconfig;
			if (sqlobj instanceof String) {
				sqlconfig = SysSql.getSqlid((String) sqlobj, dbtype);
				// 当前执行的sqlid
				String exeSqlid = sqlconfig.getSqlid();
				if (dolog) {
					log.info("##### comn update exeid : " + execonfig.getExeid() + ", execute sqlid : " + exeSqlid
							+ ", " + dbtype);
				}
			} else {
				sqlconfig = (SqlConfig) sqlobj;
				if (dolog) {
					log.info("##### comn query exeid : " + execonfig.getExeid() + ", execute sqlname : "
							+ sqlconfig.getSqlname() + ", " + dbtype);
				}
			}

			// 准备更新执行
			ISqlStatement stmExecute;
			if (SqlUtil.isPrepared) {
				stmExecute = new PreparedSqlStatement(sqlconfig.getSql(dbtype), getJdbcTemplate());
			} else {
				stmExecute = new SqlStatement(sqlconfig.getSql(dbtype), getJdbcTemplate());
			}

			// 进行SQL执行前校验
			if (sqlCheck(getJdbcTemplate(), sqlconfig, params)) {// SQL校验成功
				// 设置参数，但不设置分页参数，
				stmExecute.autoSetParams(params, false);
				// 执行更新语句
				int ret = stmExecute.executeUpdate();

				executedSqlids.add(sqlconfig.getSqlid());// 收集执行的sqlid
				fetchRows.add(ret);// 收集执行影响行数
			}
			// line++;
		} // end for
		descript += "成功";

		// // 把所有更新影响的行数保存到params中，以便action中读取用以返回
		// params.put(SqlUtil.COMN_UPDATE_FETCH_ROWS_KEY,
		// fetchRows.toArray(Tools.emptyArrayInteger));
		//
		// // 更新缓存
		// SysCacheManager.flushSqlid(executedSqlids, params);

		return descript;
	}

	/**
	 * 进行SQL执行前校验
	 * 
	 * @throws KSqlException
	 * 
	 * @throws KSystemException
	 * @throws SQLException
	 * @throws KSqlException
	 * @throws KPromptException
	 * @throws KSqlException
	 */
	public boolean sqlCheck(JdbcTemplate jdbcTemplate, SqlConfig sqlinfo, Map<String, Object> params)
			throws KPromptException, KSqlException, KSystemException, SQLException {
		// 读取SQL执行得校验配置
		String dbtype = getDBType();
		List<SqlCheck> checks = sqlinfo.getChecks();
		if (checks == null)
			return true;

		boolean checkOK = true;// SQL校验是否成功，如果此变量值为false，则跳过这个SQL更新
		// 进行SQL校验检查
		for (SqlCheck check : checks) {// 遍历单个SQL更新的校验配置信息
			String checkname = check.getCheckname();// 校验名称
			String checkSql = check.getChecksql(dbtype);// 查询SQL
			String checkexeid = check.getChecksqlid();// 通用查询exeid
			String checkString = check.getCheckstring();// 校验值
			String compareVal = check.getCompareval();// 比较值
			String compareSign = check.getComparesign();// 比较条件
			String errText = check.getErrtext();// 出错提示
			Boolean exitAll = check.getExitall();// 是否中止同步SQL执行
			Object checkVal = null;// 校验值
			if (!Tools.strIsEmpty(checkexeid)) {
				SqlResult sr = this.query(checkexeid, params);
				if (sr.next()) {// SQL执行成功，进行校验检查
					checkVal = sr.getByColumnIndex(0);// 只取查询结果的第一行第一列的值
				}
			} else if (!Tools.strIsEmpty(checkSql)) {
				SqlStatement sm = new SqlStatement(checkSql, jdbcTemplate);
				sm.autoSetParams(params, false);// 设置参数，但不设置分页参数
				// 执行校验检查SQL
				SqlResult sr = sm.executeQuery();
				if (sr.next()) {// SQL执行成功，进行校验检查
					checkVal = sr.getByColumnIndex(0);// 只取查询结果的第一行第一列的值
				}
			} else if (!Tools.strIsEmpty(checkString)) {
				StringBuilder sb = new StringBuilder(checkString.trim());
				// boolean isNumber = sb.indexOf("$N{") == 0;
				// 参数注入
				SqlStatement.autoSetParams(sb, params, false);
				checkVal = sb.toString();
				// if (isNumber && Tools.isNumber((String) checkVal))
				if (Tools.isNumber((String) checkVal)) {
					checkVal = Tools.str2BigDecimal((String) checkVal);
				}
			} else {
				throw new KSystemException(
						"通用更新的SQL校验配置中有没有配置checksqlid或checksql或checkstring的配置checkname=" + checkname);
			}

			StringBuilder sbCompareVal = new StringBuilder(compareVal);
			// 给比较值注入参数值（如果有的话）
			SqlStatement.autoSetParams(sbCompareVal, params, false);
			if (compare(checkVal, sbCompareVal.toString(), compareSign)) {// 校验检查失败条件成立
				StringBuilder sbErrText = new StringBuilder(errText);
				// 给错误提示信息注入参数值（如果有的话）
				SqlStatement.autoSetParams(sbErrText, params, false);
				errText = sbErrText.toString();
				if (exitAll) {// 中止同步SQL执行，则抛出错误信息
					throw new KPromptException(errText);
				} else {// 不中止同步SQL执行，只跳过当前SQL更新的执行
					log.info("通用SQL更新的SQL校验不通过，将跳过此更新的执行：checkname=" + checkname + ", errText:" + errText);
					checkOK = false;
				}
			}
		}
		return checkOK;
	}

	/**
	 * 比较从查询取到的对象sqlObj与compareVal对象，规则是：<br />
	 * 1. 以condition指定的比较方式<br />
	 * 2. 如果sqlObj是String类型，则两个对象都以字符串形式比较，否则把两者都转换成数字来比较
	 * 
	 * @param sqlObj
	 * @param compareVal
	 * @param sign
	 *            比较方式： eql 等于, uneql 不等于, gt 大于, gteql 大于等于, lt 小于, lteql 小于等于
	 * @return
	 */
	public static boolean compare(Object sqlObj, String compareVal, String sign) {
		if (sqlObj == null)
			return false;

		int res;
		if (sqlObj.getClass().equals(String.class)) {// 字符串比较
			res = ((String) sqlObj).compareTo(compareVal);
		} else {// 数值比较
			res = Tools.str2BigDecimal(String.valueOf(sqlObj)).compareTo(Tools.str2BigDecimal(compareVal));
		}
		sign = sign.trim();
		boolean ret;
		if ("uneql".equals(sign))// 判断不等于
			ret = res != 0;
		else
			ret = sign.indexOf("eql") > -1 && res == 0 || sign.indexOf("gt") > -1 && res > 0
					|| sign.indexOf("lt") > -1 && res < 0;
		log.info("##### compare : " + sqlObj + " " + sign + " " + compareVal + " " + ret);
		return ret;
	}

	/**
	 * 执行指定的SQL查询
	 * 
	 * @param sql
	 *            要查询的SQL
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	public SqlResult sqlQuery(String sql, String dataSource)
			throws KSqlException, KSystemException, SQLException, KSqlException {
		return sqlQuery(sql, dataSource, null, null, null);
	}

	/**
	 * 执行指定的SQL查询
	 * 
	 * @param sql
	 *            要查询的SQL
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	public SqlResult sqlQuery(String sql, String dataSource, Map<String, Object> params)
			throws KSqlException, KSystemException, SQLException, KSqlException {
		return sqlQuery(sql, dataSource, params, null, null);
	}

	/**
	 * 执行指定的SQL查询
	 * 
	 * @param sql
	 *            要查询的SQL
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @param start
	 *            起始记录，如果传null或小于零的值，则忽略该参数
	 * @param limit
	 *            最大记录数，如果传null或小于1的值，则忽略该参数
	 * @throws SQLException
	 * @throws KSystemException
	 * @throws KSqlException
	 */
	public SqlResult sqlQuery(String sql, String dataSource, Map<String, Object> params, Integer start, Integer limit)
			throws KSqlException, KSystemException, SQLException, KSqlException {
		if (params == null)
			params = new HashMap<String, Object>();

		// 设置数据源
		SqlUtil.selectDataSource(dataSource, params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG));

		if (start != null && start > -1)
			params.put("start", start);
		if (limit != null && limit > 0)
			params.put("limit", limit);

		// 执行通用查询语句
		SqlStatement stm = new SqlStatement(sql, getJdbcTemplate());
		stm.autoSetParams(params);

		SqlResult result = stm.executeQuery();

		return result;
	}

	/**
	 * 执行更新的SQL语句
	 * 
	 * @param sql
	 *            要执行的更新SQL
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @return 返回更新影晌的行数，如果出错则返回-1
	 * @throws KSqlException
	 * @throws SQLException
	 * @throws KSystemException
	 */
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// KSqlException.class)
	public int sqlUpdate(String sql, String dataSource)
			throws KSqlException, KSystemException, SQLException, KSqlException {
		return sqlUpdate(sql, dataSource, null);
	}

	/**
	 * 执行更新的SQL语句
	 * 
	 * @param sql
	 *            要执行的更新SQL
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @return 返回更新影晌的行数，如果出错则返回-1
	 * @throws SQLException
	 * @throws KSystemException
	 * @throws KSqlException
	 */
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// KSqlException.class)
	public int sqlUpdate(String sql, String dataSource, final Map<String, Object> params)
			throws KSqlException, KSystemException, SQLException, KSqlException {
		boolean donotsaylog = false;
		if (params != null && params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG))
			donotsaylog = true;
		// 设置数据源
		SqlUtil.selectDataSource(dataSource, donotsaylog);

		// 执行通用查询语句
		SqlStatement stm = new SqlStatement(sql, getJdbcTemplate());
		stm.autoSetParams(params);

		int ret = stm.executeUpdate();

		return ret;
	}

	/**
	 * 根据sqlid查询数据库
	 * 
	 * @param sqlid
	 * @param params
	 * @return
	 * @throws KPromptException
	 * @throws KSystemException
	 * @throws SQLException
	 * @throws KSqlException
	 */
	public SqlResult doQueryBySqlid(String sqlid, final Map<String, Object> params)
			throws KPromptException, KSystemException, SQLException, KSqlException {
		return doQueryBySqlid("dsSys", sqlid, params);
	}

	/**
	 * 根据sqlid查询数据库
	 * 
	 * @param dataSoure
	 * @param sqlid
	 * @param params
	 * @return
	 * @throws KPromptException
	 * @throws KSystemException
	 * @throws SQLException
	 * @throws KSqlException
	 */
	public SqlResult doQueryBySqlid(String dataSoure, String sqlid, final Map<String, Object> params)
			throws KPromptException, KSystemException, SQLException, KSqlException {

		String dbType = getDBType();

		// 取得SQL配置信息
		final SqlConfig sqlconfig = SysSql.getSqlid(sqlid, dbType);
		if (sqlconfig == null)
			throw new KSqlException("找不到 sqlid : " + sqlid);

		if (params != null && !params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG))
			log.info("##### doQueryBySqlid query sqlid : " + sqlconfig.getSqlid());

		// 选择数据源，如果使用默认的数据源，这句代码可以不写
		SqlUtil.selectDataSource(dataSoure);
		SqlStatement stm = new SqlStatement(sqlconfig.getSql(dbType), jdbcTemplate);
		stm.autoSetParams(params);
		return stm.executeQuery();

	}

	/**
	 * 根据sqlid更新数据库
	 * 
	 * @param sqlid
	 * @param params
	 * @return
	 * @throws KPromptException
	 * @throws KSystemException
	 * @throws SQLException
	 * @throws KSqlException
	 */
	public int doUpdateBySqlid(String sqlid, final Map<String, Object> params)
			throws KPromptException, KSystemException, SQLException, KSqlException {
		return doUpdateBySqlid("dsSys", sqlid, params);
	}

	/**
	 * 根据sqlid更新数据库
	 * 
	 * @param dataSoure
	 * @param sqlid
	 * @param params
	 * @return
	 * @throws KPromptException
	 * @throws KSystemException
	 * @throws SQLException
	 * @throws KSqlException
	 */
	public int doUpdateBySqlid(String dataSoure, String sqlid, final Map<String, Object> params)
			throws KPromptException, KSystemException, SQLException, KSqlException {

		String dbType = getDBType();

		// 取得SQL配置信息
		final SqlConfig sqlconfig = SysSql.getSqlid(sqlid, dbType);
		if (sqlconfig == null)
			throw new KSqlException("找不到 sqlid : " + sqlid);

		if (params != null && !params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG))
			log.info("##### doQueryBySqlid query sqlid : " + sqlconfig.getSqlid());

		if (sqlCheck(getJdbcTemplate(), sqlconfig, params)) {// SQL校验成功

			// 选择数据源，如果使用默认的数据源，这句代码可以不写
			SqlUtil.selectDataSource(dataSoure);
			SqlStatement stm = new SqlStatement(sqlconfig.getSql(dbType), jdbcTemplate);
			stm.autoSetParams(params);
			return stm.executeUpdate();
		}

		return 0;
	}

}
