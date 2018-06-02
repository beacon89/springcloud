package com.kayak.base.system;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

import com.kayak.base.util.Tools;

public class SysUtil {
	/**
	 * 数组参数值组成字符串的分隔符
	 */
	public static final String PARAVALUE_ARRAY_SEPARATOR = "|,|";

	/**
	 * 数组参数值组成字符串的分隔符应用到java.lang.String.split方法的正则串
	 */
	public static final String PARAVALUE_ARRAY_SPLIT_SEPARATOR = PARAVALUE_ARRAY_SEPARATOR.replaceAll("\\|", "\\\\\\|");

	public static Boolean buttonAuth;

	/**
	 * 返回date的时间是否在timeRange指定的时间范围内
	 * 
	 * @param date
	 *            Date
	 * @param timeRange
	 *            String 格式：hhmm-hhmm
	 */
	public static boolean isInTimeRange(Date date, String timeRange) {
		if (date == null || timeRange == null)
			return false;

		String timeStr = Tools.dt2Time1(date);
		int time = Tools.str2Int(timeStr.substring(0, 4));

		String[] range = timeRange.split("[-]");
		if (range.length != 2)
			return false;

		int startTime = Tools.str2Int(range[0].trim());
		int endTime = Tools.str2Int(range[1].trim());

		return time >= startTime && time <= endTime;
	}

	/**
	 * 获取当前WEB应用上下文对象(SPRING)
	 */
	public static ServletContext getServletContext() {
		return ContextLoader.getCurrentWebApplicationContext().getServletContext();
	}

	/**
	 * 系统参数变量名前缀
	 */
	public static final String SYS_PARAM_PREFIX = "sys_param_";
	/**
	 * 用户信息变量名前缀
	 */
	public static final String SYS_USER_PREFIX = "sys_user_";

	/**
	 * 把当前用户登录信息参数集保存在SESSION中的key
	 */
	public static final String SYS_USER_PARAMS_SESSION_KEY = "session.sys.user.params";

	/**
	 * 取得当前用户登录信息参数集
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSysUserParams() {
		// 用户登录信息参数，先尝试从SESSION中取得
		Map<String, Object> userParams = (Map<String, Object>) RequestSupport.getLocalRequest().getSession()
				.getAttribute(SYS_USER_PARAMS_SESSION_KEY);
		return userParams;
	}

	/**
	 * 取得当前用户登录信息参数值
	 * 
	 * @throws KSqlException
	 */
	public static Object getSysUserParamValue(String pname) {
		if (!pname.startsWith(SYS_USER_PREFIX)) {
			return null;
		}
		Map<String, Object> userParams = getSysUserParams();
		if (userParams == null)
			return null;

		pname = pname.replaceFirst(SysUtil.SYS_USER_PREFIX, "");
		return userParams.get(pname);
	}

	/**
	 * 解释数据库中定义为char型，用于表示是/否的值，翻译成boolean对象返回
	 */
	public static boolean char2Boolean(String str) {
		return "Y".equalsIgnoreCase(str);
	}

	/**
	 * 将boolean值解释成数据库中定义为char型，用于表示是/否的值
	 */
	public static String boolean2Char(Boolean bool) {
		return (bool != null && bool ? "Y" : "N");
	}

	/**
	 * 将数组所有元素以PARAVALUE_ARRAY_SEPARATOR定义的分隔符串连起来组成一个字符串返回<br />
	 * （数组中的元素将被直接转成字符串）
	 * 
	 * @param objs
	 * @return
	 */
	public static String paraArray2String(Object[] objs) {
		return Tools.arrayJoin(objs, PARAVALUE_ARRAY_SEPARATOR);
	}

	public static void main(String[] args) {
		String str = "20120314181212";
		System.out.println(str.substring(0, 8));
		System.out.println(str.substring(8));
	}

}
