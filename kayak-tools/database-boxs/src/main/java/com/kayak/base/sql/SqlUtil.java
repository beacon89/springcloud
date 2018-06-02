package com.kayak.base.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

import com.kayak.base.dao.SequenceDao;
import com.kayak.base.dynamicds.CustomerContextHolder;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.system.SysBeans;
import com.kayak.base.util.Tools;

/**
 * 封装一些数据库操作时使用的方法
 */
public class SqlUtil {
	private static Logger log = Logger.getLogger(SqlUtil.class);

	/**
	 * 在自动设置SQL语句的AUTOID类型参数后，将新ID保存到params中的键
	 */
	public static final String COMN_UPDATE_NEWID_KEY = "bizr2_comn_update_newid";

	public static final String COMN_UPDATE_FETCH_ROWS_KEY = "bizr2_comn_update_fetch_rows";

	public static boolean isPrepared = false;

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量 SysBeans.getSequenceDao().newId方法的简写
	 * 
	 * @param tablename
	 * @return
	 */
	public static String newId(String tablename) {
		return newId(tablename, null);
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量 SysBeans.getSequenceDao().newId方法的简写
	 * 
	 * @param tablename
	 * @param padLeft
	 *            Integer 如果padLeft>0，则为返回的ID值左补0（padLeft指定位数）
	 * @return
	 */
	public static String newId(String tablename, Integer padLeft) {
		SequenceDao sequence = SysBeans.getSequenceDao();
		try {
			return sequence.newId(tablename, padLeft);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 保存默认数据源
	 */
	public static String defaultDataSource;

	/**
	 * 切换当前数源据源
	 * 
	 * @param dataSource
	 */
	public static void selectDataSource(String dataSource) {
		selectDataSource(dataSource, false);
	}

	/**
	 * 切换当前数源据源
	 * 
	 * @param dataSource
	 */
	public static void selectDataSource(String dataSource, boolean donotsaylog) {
		if (defaultDataSource == null) {// 设置新数据源之前，先保存默认数据源
			defaultDataSource = CustomerContextHolder.getCustomerType();
		}
		if (dataSource == null || "".equals(dataSource.trim())) {
			log.info("dataSource为空，尝试使用上次使用的数据源");
			SqlUtil.restoreDefaultDataSource(donotsaylog);
		} else {// 有设置数据源，更改数据源
			CustomerContextHolder.setCustomerType(dataSource.trim(), donotsaylog);
		}
	}

	/**
	 * 选择系统库所在数据源
	 */
	public static void selectDataSourceSys() {
		selectDataSourceSys(false);
	}

	/**
	 * 选择系统库所在数据源
	 */
	public static void selectDataSourceSys(boolean donotsaylog) {
		CustomerContextHolder.setCustomerType(getDataSourceSys(), donotsaylog);
	}

	public static String getDataSourceSys() {
		return "dsSys";
	}

	/**
	 * 切换到默认数据源
	 */
	public static void restoreDefaultDataSource() {
		restoreDefaultDataSource(false);
	}

	/**
	 * 切换到默认数据源
	 */
	public static void restoreDefaultDataSource(boolean donotsaylog) {
		if (defaultDataSource == null) {
			defaultDataSource = CustomerContextHolder.getCustomerType();
		}
		CustomerContextHolder.setCustomerType(defaultDataSource, donotsaylog);
	}

	/**
	 * 返回可直接放在SQL语句中使用的字符串<br />
	 * 对str做了以下处理：<br />
	 * 1. 单引号过滤<br />
	 * 2. 前后用单引号括起来
	 */
	public static String sqlString(String str) {
		return "'" + str2query(str) + "'";
	}

	/**
	 * 字符串直接用在sql(或hql)语句上时,必须经过此函数过滤<br />
	 * (过滤是将str里的单引号(')转换成两个单引号(''))
	 */
	public static String str2query(String str) {
		if (str == null) {
			return "";
		}
		return str.replace("'", "''");// 处理所有单引号
		// .replace("{''}", "'");//处理转意单引号
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Long对象返回，如果obj为null，则返回0
	 */
	public static Long obj2Long(Object obj) {
		if (obj == null)
			return 0l;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0l;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Integer对象返回，如果obj为null，则返回0
	 */
	public static Integer obj2Integer(Object obj) {
		if (obj == null)
			return 0;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Short对象返回，如果obj为null，则返回0
	 */
	public static Short obj2Short(Object obj) {
		if (obj == null)
			return 0;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Double对象返回，如果obj为null，则返回0
	 */
	public static Double obj2Double(Object obj) {
		if (obj == null)
			return 0d;

		try {
			return Double.parseDouble(obj.toString());
		} catch (NumberFormatException e) {
			return 0d;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为BigDecimal对象返回，如果obj为null，则返回0
	 */
	public static BigDecimal obj2BigDecimal(Object obj) {
		if (obj == null || obj.equals(Tools.ZERO_E_BIGDECIMAL))
			return new BigDecimal(0);

		try {
			return (BigDecimal) obj;
		} catch (ClassCastException e) {
			return BigDecimal.valueOf(Tools.str2Double(obj.toString()));
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为String对象返回，如果obj为null，则返回""
	 */
	public static String obj2String(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Date对象返回，如果obj为null，则返回EmptyDate
	 */
	public static Date obj2Date(Object obj) {
		if (obj == null)
			return Tools.getEmptyDate();

		try {
			return (Date) obj;
		} catch (ClassCastException e) {
			return Tools.getEmptyDate();
		}
	}

	public static byte[] emptyByteArray = new byte[] {};

	/**
	 * 获取byte[]类型的值，如果类型转换出错或者obj==null则返回一个空的byte[]对象
	 */
	public static byte[] obj2ByteArray(Object obj) {
		if (obj == null)
			return emptyByteArray;
		try {
			return (byte[]) obj;
		} catch (ClassCastException e) {
			return emptyByteArray;
		}
	}

	/**
	 * 将查询结果记录转为javabean类实例对象返回<br />
	 * 这里就要求clazz必须是个javabean类
	 */
	@SuppressWarnings("hiding")
	public static <T> T row2object(SqlRow row, Class<T> clazz) {
		try {
			T o = clazz.newInstance();
			// 在这里利用JAVA反射，去遍历o的方法，
			// 只要找到set开头的方法，把方法名称set后面的字符截出来，首字母改为小写，
			// 以这个为字段名字去row里取出值来，
			// 然后调用o的这个set方法，把值填到o里

			// 在这里补充代码，顺便学习一下JAVA反射

			Method[] ms = clazz.getDeclaredMethods();
			for (Method mt : ms) {
				// System.out.println(mt);
				String mothodName = mt.getName();
				String pre = mothodName.substring(0, 3);
				if (pre.equals("set")) {
					String name = mothodName.substring(3).toLowerCase();
					if (!row.containsKey(name)) {// 是否存在该字段名称
						continue;
					}
					// 还得判断一下方法是否有且仅有一个输入参数呢
					@SuppressWarnings("rawtypes")
					Class[] ptypes = mt.getParameterTypes();
					if (ptypes.length != 1) {// 不是仅有一个输入参数，则认为它不是setter方法
						continue;
					}
					// 最好再判断一下第一个参数的类型
					// 比较复杂，会有多种情况，暂时不判断，
					// 看使用交果如何，将来因为这个出现什么问题，再根据实际解决吧
					// Class type = ptypes[0];

					Object val = row.get(name);
					mt.invoke(o, val);
				}
			}

			// 完成所有set设置值后返回o
			return o;
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将查询结果集转换为javabean类clazz的实例list
	 */
	public static List<T> result2objlist(SqlResult sr, Class<T> clazz) {
		if (sr == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		sr.resetCursor();
		while (sr.next()) {
			try {
				list.add(row2object(sr.getRow(), clazz));
			} catch (KSqlException e) {
				log.error(e.getMessage(), e);
			}
		}
		return list;
	}

	public static synchronized boolean isPrepared() {
		return isPrepared;
	}

	public static synchronized void setPrepared(boolean isPrepared) {
		SqlUtil.isPrepared = isPrepared;
	}

	/**
	 * 从applicationContext.xml中读取数据源的连接URL，从而取得连接的数据库名称
	 */
	// public static String getDBType(String datasource)
	// {
	// CacheDataSourceProp ds = (CacheDataSourceProp)
	// SysBeans.getBean("cacheDataSourceProop");
	// return ds.getDBType(datasource);
	// }

	public static void main(String[] args) {
		double dValue = 41.261505067E8;
		System.out.println(SqlUtil.obj2Long(dValue));
	}
}
