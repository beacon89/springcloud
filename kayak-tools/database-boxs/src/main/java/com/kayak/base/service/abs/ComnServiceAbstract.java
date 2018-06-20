package com.kayak.base.service.abs;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.system.KResult;
import com.kayak.base.util.Tools;

public abstract class ComnServiceAbstract {
	protected static Logger log = Logger.getLogger(ComnServiceAbstract.class);

	/**
	 * 在ComnServiceAbstract的子类中，将以这个变量定义的值作为参数名称的MAP添加到params参数值中，
	 * 这样就可以把这个MAP中的值添加到更新返回的JSON对象的returndata中<br />
	 * <b>注：这个参数值必须是个MAP对象，其它类型无效</b>
	 */
	public static String COMN_UPDATE_ADDTO_RETURNDATA = "comn_update_addto_returndata";

	/**
	 * 要使用ComnDao对象请调用getComnDao方法
	 */
	@Resource
	private ComnDao comnDao = null;

	/**
	 * 添加pname指定的参数名称和值到更新返回前端的JSON对象的returndata中
	 * 
	 * @param params
	 *            调用doUpdate的params参数集
	 * @param pname
	 *            指定在returndata中的参数名称
	 * @param value
	 *            指定参数值
	 */
	public static void addtoReturndata(Map<String, Object> params,
			String pname, String value) {
		@SuppressWarnings("unchecked")
		Map<String, String> m = (Map<String, String>) params
				.get(COMN_UPDATE_ADDTO_RETURNDATA);
		if (m == null) {
			m = new HashMap<String, String>();
			params.put(COMN_UPDATE_ADDTO_RETURNDATA, m);
		}
		m.put(pname, value);
	}

	/**
	 * 执行通用SQL查询处理
	 * 
	 * @throws KSqlException
	 */
	abstract protected SqlResult doQuery(String exeid,
			final Map<String, Object> params) throws KPromptException;

	/**
	 * 执行通用SQL查询处理
	 * 
	 * @throws KSqlException
	 */
	public KResult comnQuery(final Map<String, Object> params)
			throws KPromptException {
		return comnQuery(null, params);
	}

	/**
	 * 执行通用SQL查询处理
	 * 
	 * @throws KSqlException
	 */
	public KResult comnQuery(String exeid, final Map<String, Object> params)
			throws KPromptException {
		if (params != null) {
			if (exeid == null)
				exeid = Tools.trimString(params.get("exeid"));
			else
				params.put("exeid", exeid);
		}

		// 执行查询，得到查询结果集
		SqlResult sresult = doQuery(exeid, params);

		// 查询结果集转成可用于返回到客户端的结果集对象
		KResult cresult = new KResult(sresult);

		if (!Tools.strIsEmpty(exeid)) {
			if (params != null) {
				// 是否显示复选框
				String checkbox = (String) params.get("checkbox");
				cresult.setCheckbox(Tools.str2Boolean(checkbox));
				cresult.setInitExpand((String) params.get("initExpand"));
				Object singleClickExpand = params.get("singleClickExpand");
				if (singleClickExpand != null) {
					cresult.setSingleClickExpand(Tools
							.str2Boolean(singleClickExpand.toString()));
				}
			}
		}
		return cresult;
	}

	/**
	 * 执行通用SQL更新处理
	 * 
	 * @return 执行成功将返回更新结果提示信息，如果SQL更新时出错则返回null
	 * @throws KPromptException
	 *             当有校验错误或其它处理错误需要返回到界面提示给用户时抛出
	 * @throws KSqlException
	 */
	abstract protected String doUpdate(String exeid,
			final Map<String, Object> params) throws KPromptException;

	/**
	 * 执行通用SQL更新<br />
	 * 在执行查询前，必须给params成员设置值，而且要把exeid添加在params中
	 * 
	 * @return 执行成功将返回更新结果提示信息，如果SQL更新时出错则返回null
	 * @throws KPromptException
	 *             在SQL校验检查失败时，抛出相应的错误信息，在调用此方法时，必须处理这个错误信息（如返回提示给用户）
	 * @throws KSqlException
	 */
	public String comnUpdate(final Map<String, Object> params)
			throws KPromptException {
		return comnUpdate(null, params);
	}

	/**
	 * 执行通用SQL更新<br />
	 * 在执行查询前，必须给params成员设置值，而且要把exeid添加在params中
	 * 
	 * @param exeid
	 *            如果指定exeid则执行exeid指定的通用SQL，否则尝试从params中取得exeid参数来使用
	 * @return 执行成功将返回更新结果提示信息，如果SQL更新时出错则返回null
	 * @throws KPromptException
	 *             在SQL校验检查失败时，抛出相应的错误信息，在调用此方法时，必须处理这个错误信息（如返回提示给用户）
	 * @throws KSqlException
	 */
	public String comnUpdate(String exeid, final Map<String, Object> params)
			throws KPromptException {
		if (params != null) {
			if (exeid == null)
				exeid = String.valueOf(params.get("exeid"));
			else
				params.put("exeid", exeid);
		}
		return doUpdate(exeid, params);
	}

	public ComnDao getComnDao() {
		return comnDao;
	}

}
