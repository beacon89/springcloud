package com.kayak.base.sql;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.kayak.base.dynamicds.CustomerContextHolder;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.exception.KSystemException;
import com.kayak.base.system.RequestSupportDb;
import com.kayak.base.system.SysBeans;
import com.kayak.base.system.SysUtil;
import com.kayak.base.util.Tools;

public class SqlStatement implements ISqlStatement {
	public static Boolean getFillNone() {
		return true;
	}

	private static Logger log = Logger.getLogger(SqlStatement.class.getName());

	private StringBuilder sbSql;

	/**
	 * 分页查询时使用：查询起始行数
	 */
	private Integer start = null;

	/**
	 * 分页查询时使用：查询返回行数
	 */
	private Integer limit = null;

	private List<String> sort = null;

	private List<String> dir = null;

	private JdbcTemplate jdbc = null;

	private final static String SYS_SUBSQL = "#";

	/**
	 * 是否输出日志，在autoSetParams中获取params中的参数自动设置
	 */
	private boolean donotsaylog = false;

	/**
	 * 创建SqlStatement实例用以执行SQL语句
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @throws KSystemException
	 */
	public SqlStatement(String sql, JdbcTemplate jdbc) throws KSystemException, SQLException {
		if (Tools.strIsEmpty(sql))
			throw new KSystemException("创建SqlStatement实例的SQL语句不能为空。");
		if (jdbc == null)
			throw new KSystemException("创建SqlStatement实例的JdbcTemplate对象不能为空。");

		this.jdbc = jdbc;
		this.sbSql = new StringBuilder(sql);
	}

	public SqlStatement(String sql) throws KSystemException {
		if (Tools.strIsEmpty(sql))
			throw new KSystemException("创建SqlStatement实例的SQL语句不能为空。");

		this.sbSql = new StringBuilder(sql);
	}

	/**
	 * 检查参数有效性
	 */
	private static boolean checkPara(String paraName, Object paraValue) {
		if (paraName == null) {
			log.error("paraName is null");
			return false;
		}
		if (paraValue == null) {
			// log.error("paraValue is null");
			// return false;
		}
		return true;
	}

	private static final String String = "S";
	private static final String Method = "M";// 自定义方法生成参数
	private static final String Number = "N";
	private static final String Unknow = "U";
	private static final String List = "L";// 字符串数组，逗号分隔，如 23,4324,21423
	private static final String AutoID = "AUTOID";
	private static final String IsNull = "ISNULL";
	private static final String IsNotNull = "ISNOTNULL";
	private static final String IdTo15 = "IDTO15";
	private static final String orgID = "ORGID";
	private static final String Subsql = "SQ";// 开发人员自定义的sql

	/**
	 * 替换参数值<br />
	 * 填充在SQL语句里以$类型{变量名}形式占位的变量为实际参数值
	 * 
	 * @param sb
	 *            StringBuilder 要替换参数的SQL语句
	 * @param paraName
	 *            参数名（即变量名）
	 * @param sqlValue
	 *            可直接接入到SQL语句里的参数值
	 * @param paraType
	 *            参数类型：<br />
	 *            "AUTOID" - 自动生成的ID值，此类型只能用于INSERT语句，用于自动生成唯一ID，<br />
	 *            "N" - 数字类型（包括：Integer,Long,Short,Double,BifDecimal），<br />
	 *            "S" - 字符串类型（String），<br />
	 *            "U" - 未知类型，将直接把参数值输出到SQL语句中，<br />
	 *            "ISNULL" - 判断该参数值是否为空（字符型:null或"",
	 *            数字型:null)，如果是，则用1=1替代，否则用1=0替代<br />
	 *            "ISNOTNULL" - 判断该参数值是否不为空（字符型:null或"",
	 *            数字型:null)，如果不为空，则用1=1替代，否则用1=0替代
	 */
	private static void replacePara(final StringBuilder sql, String paraName, String sqlValue, String paraType) {
		Pattern ptn = Pattern.compile("[$]" + paraType + "\\{" + paraName + "\\}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = ptn.matcher(sql);
		while (matcher.find()) {
			sql.replace(matcher.start(), matcher.end(), sqlValue);
			matcher.reset(sql);// 因为matcher的字符串改变了，所以要重新matcher
		}
	}

	/**
	 * 设置Integer类型参数值
	 */
	public void setInteger(String paraName, Integer paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(this.sbSql, paraName, paraValue.toString(), Number);
		}
	}

	/**
	 * 设置未知类型数组参数值，把所有数组元素用逗号拼凑起来后直接输出到SQL语句中
	 */
	public void setInUnknowParam(String paraName, Object[] paraValues) {
		setInUnknowParam(this.sbSql, paraName, paraValues);
	}

	/**
	 * 设置未知类型数组参数值，把所有数组元素用逗号拼凑起来后直接输出到SQL语句中
	 */
	public static void setInUnknowParam(final StringBuilder sql, String paraName, Object[] paraValues) {
		if (checkPara(paraName, paraValues)) {
			if (paraValues == null || paraValues.length == 0) {// 数组为0，为了避免SQL语句执行出错，填充一个空值
				replacePara(sql, paraName, SqlUtil.sqlString(""), String);
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (Object val : paraValues) {
				sb.append(",").append(SqlUtil.str2query(val.toString()));
			}
			replacePara(sql, paraName, sb.substring(1), Unknow);
		}
	}

	/**
	 * 设置不是输出到SQL语句的参数变量（直接把变量值输出，不过滤单引号）
	 */
	public static void setNot2SqlParam(final StringBuilder sb, String paraName, Object paraValue, String paraType) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sb, paraName, Tools.trimString(paraValue), paraType);
		}
	}

	/**
	 * 设置不是输出到SQL语句的数组参数变量（直接把变量值输出，不过滤单引号）
	 */
	public static void setNot2SqlParam(final StringBuilder sql, String paraName, Object[] paraValues, String paraType) {
		if (checkPara(paraName, paraValues)) {
			if (paraValues == null || paraValues.length == 0) {// 数组为0，填充一个空值
				replacePara(sql, paraName, "", paraType);
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (Object val : paraValues) {
				sb.append(",").append(val);
			}
			replacePara(sql, paraName, sb.substring(1), paraType);
		}
	}

	/**
	 * 设置未知类型参数值，直接把变量值输出到SQL语句中
	 */
	public void setUnknowParam(String paraName, Object paraValue) {
		setUnknowParam(this.sbSql, paraName, paraValue);
	}

	/**
	 * 设置未知类型参数值，直接把变量值输出到SQL语句中
	 */
	public static void setUnknowParam(final StringBuilder sql, String paraName, Object paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sql, paraName, SqlUtil.str2query(Tools.trimString(paraValue)), Unknow);
		}
	}

	public static void setAutoIDParam(final StringBuilder sql, String paraName, String paraValue) {
		replacePara(sql, paraName, paraValue, AutoID);
	}

	/**
	 * 设置String类型参数值
	 */
	public void setString(String paraName, String paraValue) {
		setString(this.sbSql, paraName, paraValue);
	}

	/**
	 * 设置String类型参数值
	 */
	public static void setString(final StringBuilder sql, String paraName, String paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sql, paraName, SqlUtil.sqlString(paraValue), String);
		}
	}

	public static void setMethod(final StringBuilder sql, String paraName, String paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sql, paraName, paraValue, Method);
		}
	}

	/**
	 * 开发人员自定义的子sql
	 * 
	 * @param sql
	 * @param paraName
	 * @param paraValue
	 */
	public static void setSubsql(final StringBuilder sql, String paraName, Map<String, Object> params) {
		String className = null;
		String[] args = null;

		// 判断是否包含参数
		if (paraName.contains(",")) {
			String[] paraNames = paraName.split(",");
			className = paraNames[0];
			args = paraNames[1].split("&");
		} else {
			className = paraName;
		}

		try {
			Class<?> subSqlClass = Class.forName(className);
			SubSql subSqlInstance = (SubSql) subSqlClass.newInstance();

			// 调用子sql方法
			String paraValue = subSqlInstance.getSubSql(args, params);

			if (checkPara(paraName, paraValue)) {
				replacePara(sql, paraName, paraValue, Subsql);
			}
		} catch (Exception e) {
			log.error(e);
		}

	}

	/**
	 * 设置Long类型参数值
	 */
	public void setLong(String paraName, Long paraValue) {
		setLong(this.sbSql, paraName, paraValue);
	}

	/**
	 * 设置Long类型参数值
	 */
	public static void setLong(final StringBuilder sql, String paraName, Long paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sql, paraName, paraValue.toString(), Number);
		}
	}

	/**
	 * 设置Short类型参数值
	 */
	public void setShort(String paraName, Short paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(this.sbSql, paraName, paraValue.toString(), Number);
		}
	}

	/**
	 * 设置Double类型参数值
	 */
	public void setDouble(String paraName, Double paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(this.sbSql, paraName, paraValue.toString(), Number);
		}
	}

	/**
	 * 设置BigDecimal类型参数值
	 */
	public void setBigDecimal(String paraName, BigDecimal paraValue) {
		setBigDecimal(this.sbSql, paraName, paraValue);
	}

	/**
	 * 设置BigDecimal类型参数值
	 */
	public static void setBigDecimal(final StringBuilder sql, String paraName, BigDecimal paraValue) {
		if (checkPara(paraName, paraValue)) {
			replacePara(sql, paraName, paraValue.toString(), Number);
		}
	}

	/**
	 * 设置BigDecimal类型参数值
	 */
	public static void setBigDecimal(final StringBuilder sql, String paraName, String paraValue) {
		if (checkPara(paraName, paraValue)) {
			if (!"".equals(paraValue)) {
				BigDecimal bparaValue = Tools.str2BigDecimal(paraValue);
				replacePara(sql, paraName, bparaValue.toString(), Number);
			} else {
				replacePara(sql, paraName, "null", Number);
			}
		}
	}

	/**
	 * 设置使用在SQL的in语句里的多个字符串参数值<br />
	 * 
	 * @param paraName
	 *            参数名称
	 * @param paraValues
	 *            字符串参数值数组
	 */
	public void setInString(String paraName, String[] paraValues) {
		setInString(this.sbSql, paraName, paraValues);
	}

	/**
	 * 设置使用在SQL的in语句里的多个字符串参数值<br />
	 * 
	 * @param paraName
	 *            参数名称
	 * @param paraValues
	 *            字符串参数值数组
	 */
	public static void setInString(final StringBuilder sql, String paraName, Object[] paraValues) {
		if (checkPara(paraName, paraValues)) {
			if (paraValues == null || paraValues.length == 0) {// 数组为0，为了避免SQL语句执行出错，填充一个空值
				replacePara(sql, paraName, SqlUtil.sqlString(""), String);
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (Object val : paraValues) {
				sb.append(",").append(SqlUtil.sqlString(val.toString()));
			}
			replacePara(sql, paraName, sb.substring(1), String);
		}
	}

	/**
	 * 设置使用在SQL的in语句里的字符串数组（如：23,123,213）
	 * 
	 * @param sql
	 * @param paraName
	 * @param paraValues
	 */
	public static void setList(final StringBuilder sql, String paraName, String paraValues) {
		if (checkPara(paraName, paraValues)) {
			if (Tools.strIsEmpty(paraValues)) {
				replacePara(sql, paraName, SqlUtil.sqlString(""), List);
			}
			String[] values = paraValues.split(",");
			StringBuffer sb = new StringBuffer();
			for (String val : values) {
				val = Tools.trimString(val);
				sb.append(",").append(SqlUtil.sqlString(val));
			}
			replacePara(sql, paraName, sb.substring(1), List);
		}
	}

	/**
	 * 设置使用在SQL的in语句里的多个整数参数值<br />
	 * 
	 * @param paraName
	 *            参数名称
	 * @param paraValues
	 *            整数参数值数组
	 */
	public void setInInteger(StringBuilder sql, String paraName, Integer[] paraValues) {
		if (checkPara(paraName, paraValues)) {
			if (paraValues.length == 0) {// 数组为0，为了避免SQL语句执行出错，填充一个0
				replacePara(sql, paraName, "0", Number);
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (Integer val : paraValues) {
				sb.append(",").append(val);
			}
			replacePara(sql, paraName, sb.substring(1), Number);
		}
	}

	/**
	 * 设置使用在SQL的in语句里的多个数字参数值<br />
	 * 
	 * @param paraName
	 *            参数名称
	 * @param paraValues
	 *            数字参数值数组
	 * @throws KSqlException
	 */
	public static void setInNumber(final StringBuilder sql, String paraName, Object[] paraValues) throws KSqlException {
		if (checkPara(paraName, paraValues)) {
			if (paraValues == null || paraValues.length == 0) {// 数组为0，为了避免SQL语句执行出错，填充一个0
				replacePara(sql, paraName, "0", Number);
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (Object val : paraValues) {
				String v = val.toString();
				if (Tools.isNumber(v))// 是否有效果数字字符
					sb.append(",").append(v);
			}
			if (sb.length() > 1)
				replacePara(sql, paraName, sb.substring(1), Number);
			else
				throw new KSqlException("SQL语句参数" + paraName + "的值无效");
		}
	}

	/**
	 * 收集重复SQL语句
	 */
	private List<String> repeatSqls;

	/**
	 * 添加参数到params集合中，如果pname在pamras中已存在，则把已存在的值与新添加的值组成数组（把新添加的值添加到原值后面）存放
	 * 
	 * @param params
	 * @param pname
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	private static void addValue2params(final Map<String, Object> params, String pname, Object value) {
		if (params == null)
			return;

		if (SqlUtil.COMN_UPDATE_NEWID_KEY.equals(pname)) {

			Map<String, Object> comnUpdateNewid = (Map<String, Object>) params.get(SqlUtil.COMN_UPDATE_NEWID_KEY);

			if (comnUpdateNewid == null) {
				params.put(SqlUtil.COMN_UPDATE_NEWID_KEY, value);
			} else {
				Map<String, Object> comnUpdateNewid2 = (Map<String, Object>) value;
				Entry<String, Object> entry = comnUpdateNewid2.entrySet().iterator().next();
				pname = entry.getKey();
				value = entry.getValue();

				// 加入值
				addValue2params2(comnUpdateNewid, pname, value);
			}
			return;
		}

		addValue2params2(params, pname, value);

	}

	/**
	 * 添加参数到params集合中，如果pname在pamras中已存在，则把已存在的值与新添加的值组成数组（把新添加的值添加到原值后面）存放
	 * 
	 * @param params
	 * @param pname
	 * @param value
	 */
	private static void addValue2params2(final Map<String, Object> params, String pname, Object value) {
		Object obj = params.get(pname);
		if (obj == null) {
			obj = value;
		} else {
			List<Object> lst = new ArrayList<Object>();
			if (obj.getClass().isArray()) {
				Object[] objs = (Object[]) obj;
				for (Object o : objs) {
					lst.add(o);
				}
			} else {
				lst.add(obj);
			}
			lst.add(value);
			obj = lst.toArray();
		}
		params.put(pname, obj);
	}

	private void clearAUTOIDparams(final Map<String, Object> params) {
		if (params == null)
			return;

		Set<String> pnames = params.keySet();
		for (String pname : pnames) {
			replacePara(this.sbSql, pname, "", AutoID);
		}
	}

	private static final Pattern patternParam = Pattern.compile("[$]([a-zA-Z0-9]+)\\{([\\w.]+)\\}");
	private static final Pattern patternSubSql = Pattern.compile("[$](SQ)\\{(.+)\\}");
	private static final Pattern patternRepeat = Pattern
			.compile("[$]REPEAT\\[([^]]+)\\]\\s*\\[([\\w[,]]+)\\]\\s*\\[([^]]+)\\]", Pattern.CASE_INSENSITIVE);

	private static final Pattern patternAutoidParamName = Pattern.compile("([\\w]+)[,]([\\d]+)[,]?([\\w.]*)");
	/**
	 * autoid参数的特珠格式：$autoid{pname,8}
	 */
	private static final Pattern patternAutoidParam = Pattern
			.compile("[$]([aA][uU][tT][oO][iI][dD])\\{([\\w]+[,][\\d]+[,]?[\\w.]*)\\}");

	/**
	 * 自动填充实例中SQL语句中的变量值<br />
	 * 执行的处理是从SQL是提取变量名，在params中取得变量值来填充<br />
	 * 如果在params中找不到变量值，则该变量将不被处理
	 * 
	 * @param params
	 *            用于填充变量的参数集合
	 * @throws KSqlException
	 * @throws KSqlException
	 */
	public void autoSetParams(Map<String, Object> params) throws KSqlException, KSqlException {
		autoSetParams(params, true);
	}

	/**
	 * 自动填充实例中SQL语句中的变量值<br />
	 * 执行的处理是从SQL中提取变量名，在params中取得变量值来填充<br />
	 * 如果在params中找不到变量值，则该变量将不被处理
	 * 
	 * @param params
	 *            用于填充变量的参数集合
	 * @param paging
	 *            是否设置分页参数，默认为true
	 * @throws KSqlException
	 * @throws KSqlException
	 */
	public void autoSetParams(final Map<String, Object> params, boolean paging) throws KSqlException, KSqlException {
		if (params != null) {// 如果没有传params参数，则不处理：分页，排序，重复串

			// 设置是否输出日志属性
			if (params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG))
				this.donotsaylog = true;

			// 先清除AUTOID参数变量
			clearAUTOIDparams(params);

			if (paging && params.get("start") != null && params.get("limit") != null) {// 自动设置分页信息
				this.start = Tools.str2Int(params.get("start").toString());
				this.limit = Tools.str2Int(params.get("limit").toString());
			}
			if (params.get("sort") != null) {// 自动设置排序信息
				this.setSort(params.get("sort"));
				if (params.get("dir") != null) {
					this.setDir(params.get("dir"));
				}
			}

			// 收集重复的SQL语句
			List<String> repeatStatements = new ArrayList<String>();
			Matcher matcher = patternRepeat.matcher(this.sbSql);
			while (matcher.find()) {
				String separator = matcher.group(3);// 重复串间的分隔符
				String repeatver = matcher.group(2);// 重复参数名

				if (Tools.strIsEmpty(repeatver))
					throw new KSqlException("自动重复串没有定义重复参数");

				// 重复串
				String repeatStr = matcher.group(1);

				// 分析重复变量
				String[] vnames = repeatver.split("[,]");
				Map<String, Object[]> vals = new LinkedHashMap<String, Object[]>();
				int repeatNum = 0;// 重复次数
				for (String vname : vnames) {
					vname = vname.trim();
					if (!params.containsKey(vname)) {
						// throw new KSqlException("自动重复串的重复参数 " + vname +
						// " 不存在");
						log.error("自动重复串的重复参数 " + vname + " 不存在");
					}
					Object val = params.get(vname);
					if (val == null) {// 重复变量值为null，则不输出重复串
						repeatNum = 0;
						break;
					}

					if (val.getClass().isArray()) {// 如果是数组，则所有重复参数值都必须都是数组，而且必须位数相同
						Object[] objs = (Object[]) val;
						if (repeatNum == 0)
							repeatNum = objs.length;// 重复次数为数组位数
						else if (repeatNum != objs.length)
							throw new KSqlException("自动重复串的重复参数值数组位数不相等");

						vals.put(vname, objs);
					} else {// 如果不是数组，则所有重复参数都必须不是数组（或只有一位）
						if (repeatNum == 0)
							repeatNum = 1;// 重复次数为1
						else if (repeatNum != 1)
							throw new KSqlException("自动重复串的重复参数值数组位数不相等");

						vals.put(vname, new Object[] { val });
					}
				} // end for
					// 收集处理好的重复串
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < repeatNum; i++) {
					// 拼凑重复参数集
					Map<String, Object> pmap = new LinkedHashMap<String, Object>();
					Set<Map.Entry<String, Object[]>> meset = vals.entrySet();
					Set<String> pkeys = new HashSet<String>();// 收集pmap的key
					for (Map.Entry<String, Object[]> me : meset) {// 参数值应该是所有重复参数的相同位数上的值
						pmap.put(me.getKey(), me.getValue()[i]);
						pkeys.add(me.getKey());
					}
					// 添加数组下标位数参数
					pmap.put("INDEX", i);
					pkeys.add("INDEX");
					StringBuilder sql = new StringBuilder(repeatStr);
					// 设置重复参数值
					autoSetParams(sql, pmap);

					if ("statement".equals(separator)) {// SQL语句重复
														// 遍历pmap如果有增加的key，则添加到params中
														// （多数情况下是AUTOID型参数变量被添加了）
						Set<Map.Entry<String, Object>> ms = pmap.entrySet();
						for (Map.Entry<String, Object> me : ms) {// 参数值应该是所有重复参数的相同位数上的值
							if (!pkeys.contains(me.getKey())) {
								addValue2params(params, me.getKey(), me.getValue());
							}
						}

						// 设置重复SQL语句的普通参数值
						autoSetParams(sql, params);
						// 收集SQL语句
						repeatStatements.add(sql.toString());
					} else {// 普通重复串
						if (result.length() > 0)// 添加分隔符
							result.append(separator);
						result.append(sql);
					}
				} // end for
				this.sbSql.replace(matcher.start(), matcher.end(), result.toString());
				matcher.reset(this.sbSql);// 因为matcher的字符串改变了，所以要重新matcher
			} // end while
			if (repeatStatements.size() > 0) {// 出现SQL语句重复，保存重复SQL语句集合
				this.repeatSqls = repeatStatements;
				return;
			} else {
				this.repeatSqls = null;
			}
		} // end if params!=null
			// 自动设置普通参数值
		autoSetParams(this.sbSql, params);
	}

	/**
	 * 自动注入参数值到SQL语句中
	 * 
	 * @param sql
	 * @param params
	 * @throws KSqlException
	 * @throws KSqlException
	 */
	public static void autoSetParams(final StringBuilder sql, final Map<String, Object> params)
			throws KSqlException, KSqlException {
		autoSetParams(sql, params, true);
	}

	/**
	 * 自动注入参数值到字符串中
	 * 
	 * @param sql
	 *            要注入的字符串
	 * @param params
	 *            提供注入的参数集
	 * @param toSql
	 *            注入的字符串是否作为SQL语句使用
	 * @throws KSqlException
	 * @throws KSqlException
	 */
	public static void autoSetParams(final StringBuilder sql, final Map<String, Object> params, boolean toSql)
			throws KSqlException, KSqlException {
		matcherProcess(patternParam, sql, params, toSql);
		matcherProcess(patternSubSql, sql, params, toSql);
		matcherProcess(patternAutoidParam, sql, params, toSql);
	}

	/**
	 * 根据正则处理注入参数值
	 * 
	 * @param sql
	 *            要注入的字符串
	 * @param params
	 *            提供注入的参数集
	 * @param toSql
	 *            注入的字符串是否作为SQL语句使用
	 * @throws KSqlException
	 * @throws KSqlException
	 */
	@SuppressWarnings("unchecked")
	private static void matcherProcess(final Pattern pattern, final StringBuilder sql, final Map<String, Object> params,
			boolean toSql) throws KSqlException, KSqlException {
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String type = matcher.group(1); // 参数类型
			String pName = matcher.group(2).trim();// 参数名称
			boolean replaced = false;// 标识sql是否有被改动，如果有根据此标识需要matcher.reset
			if (!"".equals(pName)) {
				if (toSql && AutoID.equals(type)) {// 自动生成ID类型，此类型不需要使用客户端提交的参数，将会自动生成一个并且添加到参数集合中
					String tablename = getInsertTablename(sql.toString());
					Integer padLeft = null;// 用于截取左补0位数
					String pname = pName;// 如果有设置左补0参数，则用这个变量保存截取的参数名称
					String prefix = null;
					Matcher mth = patternAutoidParamName.matcher(pName);
					if (mth.find())// autoid指定了左补0位数
					{
						pname = mth.group(1);// 截取参数名称
						padLeft = Tools.str2Int(mth.group(2));// 截取左补0位数
						prefix = mth.group(3);// ID前缀
						if (prefix != null && !"".equals(prefix)) {
							String val = null;
							if (params != null)
								val = (String) params.get(prefix);
							if (val == null)// 取不到参数值，尝试从用户登录信息中取值
								val = (String) SysUtil.getSysUserParamValue(prefix);
							// if (val == null)// 取不到参数值，尝试从系统动态参数中取值
							// val = (String)
							// SysUtil.getSysDynamicParamValue(prefix, params);
							prefix = val;
							if (prefix != null && padLeft != null) {
								padLeft -= prefix.length();
							}
						}
					}
					// 获取自动ID值
					String newid = SqlUtil.newId(tablename, padLeft);
					if (prefix != null) {// 为ID添加前缀
						newid = prefix + newid;
					}
					addValue2params(params, pname, newid);// 添加生成的ID值到参数集合中
					// 添加更新产生的新ID对象到params中，供ComnUpdateAction中获取并返回给到前端页面
					Map<String, String> comnUpdateNewid = null;
					if (params != null) {
						comnUpdateNewid = (Map<String, String>) params.get(SqlUtil.COMN_UPDATE_NEWID_KEY);
						if (comnUpdateNewid == null) {
							Map<String, String> map = new LinkedHashMap<String, String>();
							comnUpdateNewid = map;
							params.put(SqlUtil.COMN_UPDATE_NEWID_KEY, comnUpdateNewid);
						}
						comnUpdateNewid.put(pname, newid);
					}
					// 设置自动ID值
					setAutoIDParam(sql, pName, newid);
					replaced = true;
				} else if (Subsql.equalsIgnoreCase(type)) {// 开发人员自定义的子sql
					if (!Tools.strIsEmpty(pName)) {
						replaced = true;
						setSubsql(sql, pName, params);
					}
				} else {
					Object val = null;
					if (params != null)
						val = params.get(pName);

					if (val == null)// 取不到参数值，尝试从用户登录信息中取值
						val = SysUtil.getSysUserParamValue(pName);
					// if (val == null)// 取不到参数值，尝试从系统参数中取值
					// val = SysUtil.getSysParamValue(pName);
					// if (val == null)// 取不到参数值，尝试从系统动态参数中取值
					// val = SysUtil.getSysDynamicParamValue(pName, params);

					if (val != null || (params != null && params.containsKey(pName))) {
						boolean isarray = false;
						if (val != null)
							isarray = val.getClass().isArray();

						replaced = true;

						if (!toSql) {// 不是注入到SQL语句的参数值，特殊用法
							if (isarray) {
								setNot2SqlParam(sql, pName, (Object[]) val, type);
							} else {
								setNot2SqlParam(sql, pName, (Object) val, type);
							}
						} else if (Unknow.equalsIgnoreCase(type)) {// 未知类型，直接输出内容
							if (isarray) {
								setInUnknowParam(sql, pName, (Object[]) val);
							} else {
								setUnknowParam(sql, pName, (Object) val);
							}
						} else if (String.equalsIgnoreCase(type)) {
							if (isarray) {// String[]字符串数组
								setInString(sql, pName, (Object[]) val);
							} else {// 字符串
								setString(sql, pName, Tools.trimString(val));
							}
						} else if (Number.equalsIgnoreCase(type)) {
							if (isarray) {// 数组
								setInNumber(sql, pName, (Object[]) val);
							} else {// 数字
								setBigDecimal(sql, pName, Tools.trimString(val));
							}
						} else if (List.equalsIgnoreCase(type)) {
							setList(sql, pName, Tools.trimString(val));
						} else if (IsNull.equalsIgnoreCase(type)) {// 注入变量值是否为空的判断
							String value;
							if (val == null || "".equals(Tools.trimString(val))) {// 变量值为空，注入1=1
								value = "1=1";
							} else {// 变量值不为空，注入1=0
								value = "1=0";
							}
							replacePara(sql, pName, value, IsNull);
						} else if (IsNotNull.equalsIgnoreCase(type)) {// 注入变量值是否不为空的判断
							String value;
							if (val == null || "".equals(Tools.trimString(val))) {// 变量值为空，注入1=0
								value = "1=0";
							} else {// 变量值不为空，注入1=1
								value = "1=1";
							}
							replacePara(sql, pName, value, IsNotNull);
						} else if (IdTo15.equalsIgnoreCase(type)) {// 将18位身份证号转成15位，再以S类型注入值
																	// 转成15位后以字符串形式注入值
							replacePara(sql, pName, SqlUtil.sqlString(Tools.id18To15(Tools.trimString(val))), IdTo15);
						} else if (orgID.equalsIgnoreCase(type)) {
							replacePara(sql, pName, Tools.sqlOrgnoToID(Tools.trimString(val)), orgID);
						} else {
							replaced = false;
							log.error("不能识别的参数类型：$" + type + "{" + pName + "}");
						}
					} else {
						// log.error("参数：" + pName + " 不存在，无法完成注入");
					}
				}
			} // end if
			if (replaced)
				matcher.reset(sql);// 因为matcher的字符串改变了，所以要重新matcher
		} // end while
	}

	private static String getInsertTablename(final String insertSql) throws KSqlException {
		String sql = insertSql.replaceAll("\r", " ").replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("=", " = ")
				.replaceAll("[(]", " (").replaceAll("[)]", ") ").trim();
		String[] sqllower = sql.toLowerCase().split("[ ]");
		if ("insert".equals(sqllower[0]) && "into".equals(sqllower[1])) {
			return sqllower[2];
		} else {
			throw new KSqlException("参数类型 $AUTOID 只能使用在INSERT语句中");
		}
	}

	/**
	 * 分页查询时使用：设置查询起始行
	 */
	public void setStart(int num) {
		this.start = new Integer(num);

	}

	/**
	 * 分页查询时使用：设置查询返回行数
	 */
	public void setLimit(int num) {
		this.limit = new Integer(num);

	}

	/**
	 * 返回是否有分页设置（即设置了start和limit两个值）
	 */
	public boolean isPaging() {

		return start != null && start > -1 && limit != null && limit > 0;
	}

	public String getQueryString() {
		return getQueryString(!donotsaylog);
	}

	/**
	 * 返回查询的SQL语句<br />
	 * （如果有分页设置，则返回的SQL语句里包括自动添加的分页代码）<br />
	 * 目前分页支持db2, oracle, mysql，如果需要使用在其它数据库上，请在这个方法里添加相关实现
	 */
	public String getQueryString(boolean saylog) {
		if (isPaging()) {// 有分页
			int limit = this.start + this.limit;
			// int limit = this.limit;
			int start = this.start + 1;
			StringBuilder sb = new StringBuilder();
			String dbname = SysBeans.getSysSql().getDBType(CustomerContextHolder.getCustomerType());
			// String dbname = getDBname();
			if ("db2".equalsIgnoreCase(dbname)) {
				sb.append("SELECT * from (SELECT rownumber() over() as rownumPageSql,pageSqlTemp.* from (")
						.append(setOrderString(sbSql.toString()))
						.append(") pageSqlTemp)pageSqlTemp2 where pageSqlTemp2.rownumPageSql between ").append(start)
						.append(" and ").append(limit);
			} else if ("oracle".equalsIgnoreCase(dbname)) {
				sb.append("SELECT * from (SELECT rownum as rownumPageSql,pageSqlTemp.* from (")
						.append(setOrderString(sbSql.toString()))
						.append(") pageSqlTemp)pageSqlTemp2 where pageSqlTemp2.rownumPageSql between ").append(start)
						.append(" and ").append(limit);
			} else if ("mysql".equalsIgnoreCase(dbname)) {
				sb.append("SELECT * FROM (").append(setOrderString(sbSql.toString())).append(") pageTab limit ")
						.append(this.start).append(",").append(this.limit);
			} else {
				sb = sbSql;
			}
			if (saylog)
				log.info("##### sql statement : " + sb);
			return sb.toString();
		} else {
			String sql = setOrderString(sbSql.toString());
			if (saylog)
				log.info("##### sql statement : " + sql);
			return sql;
		}
	}

	private String setOrderString(String sql) {
		if (this.sort != null && this.sort.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < this.sort.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(this.sort.get(i));
				if (this.dir != null && this.dir.size() > i) {
					sb.append(" ").append(this.dir.get(i));
				}
			}
			return " SELECT * FROM ( " + sql + " ) t ORDER BY " + sb;
		}
		return sql;
	}

	/**
	 * 返回查询总记录数的SQL语句
	 */
	public String getCountQueryString() {
		String countSql = " SELECT COUNT(1) FROM ( " + sbSql + " ) t ";
		if (!donotsaylog)
			log.info(countSql);
		return countSql;
	}

	/**
	 * 返回执行更新操作的SQL语句
	 */
	public String getUpdateString() {
		if (!donotsaylog)
			log.info(sbSql);
		return this.sbSql.toString();
	}

	/**
	 * 返回总记录数
	 */
	public long executeCountQuery() throws SQLException {
		if (this.jdbc != null) {
			long st = (new Date()).getTime();
			if (!donotsaylog)
				log.info(java.lang.String.format("##### count query start: %d", st));
			SqlRowSet result = this.jdbc.queryForRowSet(this.getCountQueryString());

			long ed = (new Date()).getTime();
			if (!donotsaylog)
				log.info(java.lang.String.format("##### conut query end: %d, last: %dms", ed, ed - st));
			if (result.next()) {
				return Long.valueOf(result.getObject(1).toString());
			}
		} else {
			throw new SQLException("SqlStatement对象的jdbc为空");
		}
		return 0;
	}

	/**
	 * 检查Sql语句中的参数是否全部已设置，如果不是，则抛出错误
	 * 
	 * @throws KSqlException
	 */
	public static void checkParamFilling(final StringBuilder sql) throws KSqlException {
		checkParamFilling(sql, getFillNone());
	}

	/**
	 * 检查Sql语句中的参数是否全部已设置，如果不是，则抛出错误
	 * 
	 * @throws KSqlException
	 */
	public static void checkParamFilling(final StringBuilder sql, Boolean fillNone) throws KSqlException {
		if (fillNone == null) {
			// 从全局配置文件中读取未注入参数处理方式
			fillNone = getFillNone();
		}

		List<String> pNames = null;
		Map<String, Object> pms = null;
		if (fillNone) {// 如果需要注入空参数，则用pms生成空参数集
			pms = new HashMap<String, Object>();
		} else {// 如果不需要注入空参数，则收集未设置的参数名称，用于提示错误
			pNames = new ArrayList<String>();// 未设置的参数名称包括$符和大括号集合
		}
		Matcher matcher = patternParam.matcher(sql);
		while (matcher.find()) {
			if (fillNone) {// 如果需要注入空参数，收集参数名称，生成空参数集
				String pname = matcher.group(2).trim();// 参数名称
				pms.put(pname, "");
			} else {
				String lname = sql.substring(matcher.start(), matcher.end()); // 包括类型的名称
				if (!pNames.contains(lname))
					pNames.add(lname);
			}
		}
		if (fillNone) {// 为未注入的参数填空值
			try {
				autoSetParams(sql, pms);
			} catch (KSqlException e) {
				log.error(e.getMessage(), e);
			}
		} else if (pNames != null && pNames.size() > 0) {// 直接报错
			log.error("==================== SQL Statement ====================\n" + sql);
			throw new KSqlException("SQL语句的 " + Tools.listJoin(pNames) + " 参数未设置");
		}
	}

	/**
	 * 执行查询操作并返回SqlResult类型查询结果实例<br />
	 * <b>注：查询SQL不支持重复statement</b>
	 */
	public SqlResult executeQuery() throws KSqlException, SQLException {
		if (this.repeatSqls != null) {
			throw new KSqlException("查询SQL不支持重复statement");
		}

		// 检查参数设置
		checkParamFilling(this.sbSql);

		SqlResult result;
		if (jdbc != null) {
			if (!donotsaylog)
				log.info(java.lang.String.format("##### query start: %d", (new Date()).getTime()));
			result = jdbc.query(this.getQueryString(), new KResultSetExtractor());
			if (!donotsaylog)
				log.info(java.lang.String.format("##### query end: %d", (new Date()).getTime()));
		} else {
			throw new KSqlException("SqlStatement对象的jdbc为空");
		}

		// 给查询结果实列设置总记录数值
		if (isPaging()) {// 如果有分页，则需要另外查询总记录数
			result.setCount(executeCountQuery());
			// result.setCount(result.getRowSize());
		} else {// 没有分页，则返回结果集的记录数就是总记录数
			result.setCount(result.getRowSize());
		}

		return result;
	}

	/**
	 * 执行更新操作
	 * 
	 * @return 普通更新操作，则返回更新的记录数<br />
	 *         如果出现多SQL语句重复拼凑（$REPEAT[][][]的第三个中括号内值为statement）时<br />
	 *         将批量执行重复的SQL语句，并且返回执行的SQL语句数
	 * @throws KSqlException
	 * @throws SQLException
	 */
	public int executeUpdate() throws KSqlException, SQLException {
		if (this.repeatSqls == null) {// 不是多SQL执行，直接执行stm中的SQL

			// 检查参数设置
			checkParamFilling(this.sbSql);

			String updateSql = this.getUpdateString().trim();
			if (Tools.strIsEmpty(updateSql)) {// sql语句为空，直接返回0
				return 0;
			}
			// 执行SQL更新，并返回更新的行数
			if (jdbc != null) {
				return this.jdbc.update(updateSql);
			} else {
				throw new KSqlException("SqlStatement对象的jdbc为空");
			}
		} else {// 如果重复SQL语句集合非null，则需要执行多SQL

			boolean hasNotEmptySql = false;
			for (String sql : this.repeatSqls) {// 添加批SQL
				if (!Tools.strIsEmpty(sql)) {
					// 检查参数设置
					checkParamFilling(new StringBuilder(sql));
					hasNotEmptySql = true;// 有非空SQL语句，这将决定最后是否批执行SQL
				}
			}
			if (!donotsaylog)
				log.info("==================== execute batch sql ====================\n"
						+ Tools.listJoin(this.repeatSqls, "\n"));
			if (hasNotEmptySql) {
				// 批执行SQL
				int[] ret;
				if (this.jdbc != null) {
					ret = this.jdbc.batchUpdate(this.repeatSqls.toArray(Tools.emptyArrayString));
				} else {
					throw new KSqlException("SqlStatement对象的jdbc或statement为空");
				}
				return ret.length;// 返回执行的SQL更新语句数
			} else {
				return 0;
			}
		}
	}

	public static void main(String[] args) {
		String str = "abc,8";
		Matcher matcher = patternAutoidParamName.matcher(str);
		while (matcher.find()) {
			System.out.println("##### " + matcher.group(1));
			System.out.println("##### " + matcher.group(2));
		}

		System.out.println("jfkdjf(fjdkfjd)".replaceAll("[(]", " ("));

		// String sql =
		// "$REPEAT[INSERT INTO sys_user_role\r\t\n (userid,roleid) VALUES
		// ($S{userid},$S{roleid})]\n"
		// + "[roleid,userid]\n [;]";
		// Pattern ptn =
		// Pattern.compile("[$]REPEAT\\[([^]]+)\\]\\s*\\[([\\w[,]]+)\\]\\s*\\[(.+)\\]",
		// Pattern.CASE_INSENSITIVE);
		// matcher = ptn.matcher(sql);
		// while (matcher.find())
		// {
		// String match = sql.substring(matcher.start(), matcher.end());
		// System.out.println("##### " + match);
		// System.out.println("##### " + matcher.group(1));
		// System.out.println("##### " + matcher.group(2));
		// System.out.println("##### " + matcher.group(3));
		// }
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public List<String> getSort() {
		return sort;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setSort(Object sort) {
		if (sort == null) {
			return;
		}
		if (sort.getClass().isArray()) {
			List<String> l = new ArrayList<String>();
			Object[] sorts = (Object[]) sort;
			for (int i = 0; i < sorts.length; i++) {
				l.add(sorts[i].toString());
			}
			this.setSort(l);
		} else if (sort instanceof Collection) {
			List<String> l = new ArrayList<String>();
			l.addAll((Collection) sort);
			this.setSort(l);
		} else {
			this.setSort(sort.toString());
		}
	}

	public void setSort(String sort) {
		this.sort = new ArrayList<String>();
		this.sort.add(sort);
	}

	public void setSort(List<String> sort) {
		this.sort = sort;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setDir(Object dir) {
		if (dir == null) {
			return;
		}
		if (dir.getClass().isArray()) {
			List<String> l = new ArrayList<String>();
			Object[] dirs = (Object[]) dir;
			for (int i = 0; i < dirs.length; i++) {
				l.add(dirs[i].toString());
			}
			this.setDir(l);
		} else if (dir instanceof Collection) {
			List<String> l = new ArrayList<String>();
			l.addAll((Collection) dir);
			this.setDir(l);
		} else {
			this.setDir(dir.toString());
		}
	}

	public void setDir(String dir) {
		this.dir = new ArrayList<String>();
		this.dir.add(dir);
	}

	public List<String> getDir() {
		return dir;
	}

	public void setDir(List<String> dir) {
		this.dir = dir;
	}

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public List<String> getRepeatSqls() {
		return repeatSqls;
	}

	public void setRepeatSqls(List<String> repeatSqls) {
		this.repeatSqls = repeatSqls;
	}
}
