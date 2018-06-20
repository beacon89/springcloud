package com.kayak.base.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kayak.base.util.Tools;

public class RequestSupport {
	protected static Logger log = Logger.getLogger(RequestSupport.class);

	public final static JSONObject emptyJsonObject = new JSONObject();

	public final static JSONArray emptyJsonArray = new JSONArray();

	/**
	 * 标志不打印日志的参数变量名称
	 */
	public final static String DO_NOT_SAY_LOG = "bizr2_donotsaylog";

	/**
	 * 用于保存访问的请求对象
	 */
	protected static ThreadLocal<HttpServletRequest> tlLocalRequest = new ThreadLocal<HttpServletRequest>();

	protected static ThreadLocal<Map<String, Object>> otherparams = new ThreadLocal<Map<String, Object>>();

	/**
	 * 保存响应返回的数据
	 */
	protected static ThreadLocal<JSONObject> responseParams = new ThreadLocal<JSONObject>();

	/**
	 * 获取响应的数据
	 * 
	 * @return
	 */
	public static JSONObject getResponseParams() {
		return responseParams.get();
	}

	/**
	 * 设置响应数据;
	 * 
	 * @param json
	 */
	public static void setResponseParams(JSONObject json) {
		clearResponseParams();
		responseParams.set(json);
	}

	/**
	 * 清楚响应的数据缓存
	 */
	public static void clearResponseParams() {
		responseParams.remove();
	}

	/**
	 * 保存当前请求对象
	 */
	public static void setLocalRequest(HttpServletRequest request) {
		tlLocalRequest.set(request);
	}

	/**
	 * 获取当前请求对象
	 */
	public static HttpServletRequest getLocalRequest() {
		return tlLocalRequest.get();
	}

	/**
	 * 取得参数集<br />
	 * 对从request中取得的原始参数集Map进行处理后返回
	 * 
	 * @param paramMap
	 *            从request中取得的参数parameter原始Map对象
	 * @param transCharset
	 *            取参数值时是否进行编码转换（转换成系统编码），默认false
	 * @return
	 */
	public static Map<String, Object> convertRequestParameters(@SuppressWarnings("rawtypes") Map paramMap,
			boolean transCharset) {
		@SuppressWarnings("rawtypes")
		Map pm = paramMap;
		@SuppressWarnings("rawtypes")
		Iterator it = pm.keySet().iterator();
		Map<String, Object> map = new HashMap<String, Object>();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object val;
			if (transCharset) {// 转换String值编码
				val = Tools.string2SysCharset(pm.get(key));
			} else {
				val = pm.get(key);
			}
			if (val != null && val.getClass().isArray()) {
				Object[] vals = (Object[]) val;
				if (vals.length == 1) {
					val = vals[0];
				}
			}
			val = paramJson2map(val);
			if (val != null && val instanceof String) {
				val = ((String) val).trim();
			}
			map.put(key, val);
		}
		return dealNullParams(map);
	}

	protected static Object paramJson2map(Object val) {
		if (val == null) {
			return null;
		}
		if (val.getClass().isArray()) {
			Object[] vals = (Object[]) val;
			Object[] newvals = new Object[vals.length];
			for (int i = 0; i < vals.length; i++) {
				newvals[i] = paramJson2map(vals[i]);
			}
			return (Object) newvals;
		}
		String str = val.toString();
		if (str.startsWith("{[json]}")) {
			str = str.substring(8);
			try {
				if (!Tools.strIsEmpty(str) && !"null".equals(str))
					return (Object) Tools.json2map(new JSONObject(str));
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
				return (Object) str;
			}
		}
		return val;
	}

	/**
	 * 因为前台送的参数值不管是null或是""（空字符串），后台这里接收时都会是""（空字符串）<br />
	 * 这样后台就不能判断真正null值，所以在这里约定：<br />
	 * 前台送参数时，如果是null的，就送一个"null"字符串过来<br />
	 * 后台接收参数时，把值为"null"字符串的参数值置null
	 */
	public static Map<String, Object> dealNullParams(Map<String, Object> params) {
		Map<String, Object> pms = new HashMap<String, Object>();

		Set<Map.Entry<String, Object>> set = params.entrySet();
		for (Map.Entry<String, Object> en : set) {
			String key = en.getKey();
			Object value = en.getValue();

			if (value == null) {
				pms.put(key, null);
			} else if (value.getClass().isArray()) {
				Object[] objs = (Object[]) value;
				List<Object> list = new ArrayList<Object>();
				for (Object obj : objs) {

					if ("null".equals(obj)) {
						list.add(null);
					} else {
						list.add(obj);
					}
				}
				pms.put(key, list.toArray());
			} else if ("null".equals(value)) {
				pms.put(key, null);
			} else {
				pms.put(key, value);
			}
		}
		return pms;
	}

	/**
	 * 清除threadlocal中的参数
	 */
	public static void clearUserParams() {
		if (otherparams.get() != null) {
			otherparams.set(null);
		}
	}

	/**
	 * 手动设置流程中的参数
	 * 
	 * @param map
	 */
	public static void setUserParameters(Map<String, Object> map) {
		Map<String, Object> p = otherparams.get();
		if (p == null) {
			p = new HashMap<String, Object>();
			otherparams.set(p);
		}
		otherparams.get().putAll(convertRequestParameters(map, false));
	}

	public static JSONObject getJSONParameters() {
		return new JSONObject(getParameters());
	}

	public static JSONObject getJSONParameters(boolean doNotSayLog) {
		return new JSONObject(getParameters(doNotSayLog));
	}

	/**
	 * 取得当前请求的参数集
	 * 
	 * @return
	 */
	public static Map<String, Object> getParameters() {
		return getParameters(false, null);
	}

	/**
	 * 取得当前请求的参数集
	 * 
	 * @return
	 */
	public static Map<String, Object> getParameters(boolean doNotSayLog) {
		return getParameters(doNotSayLog, null);
	}

	/**
	 * 取得当前请求的参数集
	 * 
	 * @return
	 */
	public static Map<String, Object> getParameters(DefaultMultipartHttpServletRequest mult_request) {
		return getParameters(false, mult_request);
	}

	/**
	 * 取得当前请求的参数集
	 * 
	 * @return
	 */
	public static Map<String, Object> getParameters(boolean doNotSayLog,
			DefaultMultipartHttpServletRequest mult_request) {
		// 添加一个专门的参数保存原始request参数
		// 如果有手动设置的参数,直接以该参数返回，即不使用request中的请求参数
		Map<String, Object> m = otherparams.get();
		if (m == null) {
			HttpServletRequest request;
			if (mult_request == null) {
				request = getLocalRequest();
			} else {
				request = mult_request;
			}
			try {
				request.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			@SuppressWarnings("rawtypes")
			Map map = null;
			map = request.getParameterMap();
			m = convertRequestParameters(map, true);
			if (!doNotSayLog && !m.containsKey(DO_NOT_SAY_LOG))
				log.info(String.format("##### request url[%s] parameters:%s", getLocalRequest().getRequestURI(),
						(new JSONObject(m)).toString()));
		}
		return m;
	}

	public static Map<String, Object> getParameters(boolean doNotSayLog, HttpServletRequest mult_request) {
		// 添加一个专门的参数保存原始request参数
		// 如果有手动设置的参数,直接以该参数返回，即不使用request中的请求参数
		Map<String, Object> m = otherparams.get();
		if (m == null) {
			HttpServletRequest request;
			if (mult_request == null) {
				request = getLocalRequest();
			} else {
				request = mult_request;
			}
			try {
				request.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			@SuppressWarnings("rawtypes")
			Map map = null;
			map = request.getParameterMap();
			m = convertRequestParameters(map, true);
			if (!doNotSayLog && !m.containsKey(DO_NOT_SAY_LOG))
				log.info(String.format("##### request url[%s] parameters:%s", getLocalRequest().getRequestURI(),
						(new JSONObject(m)).toString()));
		}
		return m;
	}

	public static String getBodyParameters(HttpServletRequest mult_request) {
		// 添加一个专门的参数保存原始request参数
		// 如果有手动设置的参数,直接以该参数返回，即不使用request中的请求参数
		HttpServletRequest request;
		if (mult_request == null) {
			request = getLocalRequest();
		} else {
			request = mult_request;
		}

		String charSet = request.getCharacterEncoding();
		int len = request.getContentLength();
		if (len > 0) {
			try {
				ServletInputStream in = request.getInputStream();
				byte[] buffer = new byte[len];
				in.read(buffer, 0, len);
				String body = new String(buffer, charSet);

				return body;

			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		return "{}";

	}

	/**
	 * 获取当前请求的参数值<br />
	 * 供bizr2.tld标签库使用的方法
	 * 
	 * @return
	 */
	public static String getRequestParamValue(String paraname) {
		Object obj = getParameters(true).get(paraname);
		if (obj == null)
			return "";
		return obj.toString();
	}

	/**
	 * 构造返回到前端的通用更新结果JSON格式字符串
	 * 
	 * @param success
	 *            更新是否成功
	 * @param returnmsg
	 *            返回信息
	 * @param returndata
	 *            返回数据
	 * @return
	 */
	public static JSONObject updateReturnJson(boolean success, String returnmsg, Map<String, Object> returndata) {
		returnmsg = Tools.trimString(returnmsg);
		JSONObject jsondata;
		if (returndata == null)
			jsondata = emptyJsonObject;
		else
			jsondata = new JSONObject(returndata);

		// 设置返回结果
		JSONObject json = new JSONObject();
		try {
			json.put("success", success);
			json.put("returnmsg", returnmsg);
			json.put("returndata", jsondata);
			String strResult = json.toString();

			log.info("##### update result : " + strResult);

			responseParams.set(json);

		} catch (JSONException e) {
			log.error(e.getMessage(), e);
		}
		return json;
	}

	public static Map<String, Object> getBodyParameters(boolean doNotSayLog, HttpServletRequest mult_request) {
		// 添加一个专门的参数保存原始request参数
		// 如果有手动设置的参数,直接以该参数返回，即不使用request中的请求参数
		Map<String, Object> m = otherparams.get();
		if (m == null) {
			HttpServletRequest request;
			if (mult_request == null) {
				request = getLocalRequest();
			} else {
				request = mult_request;
			}
			try {
				String data = getBodyParameters(request);
				m = Tools.json2map(new JSONObject(data));
				if (!doNotSayLog && !m.containsKey(DO_NOT_SAY_LOG))
					log.info(String.format("##### request url[%s] parameters:%s", getLocalRequest().getRequestURI(),
							(new JSONObject(m)).toString()));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return m;
	}

	public static Map<String, Object> getBodyParameters() {
		return getBodyParameters(false, null);
	}

}
