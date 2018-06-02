package com.kayak.base.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.service.abs.ComnServiceAbstract;
import com.kayak.base.sql.SqlResult;

@Service
public class ComnService extends ComnServiceAbstract {
	/**
	 * 执行通用SQL查询处理
	 * 
	 * @return 返回查询结果集对象
	 * @throws KSqlException
	 */
	public SqlResult doQuery(String exeid, final Map<String, Object> params)
			throws KPromptException {
		// 执行查询，得到查询结果集
		try {
			return getComnDao().query(exeid, params);
		} catch (KSqlException e) {
			log.error(e.getMessage(), e);
			throw new KPromptException(e.getMessage());
		}

	}

	/**
	 * 执行通用SQL更新处理
	 * 
	 * @return 执行成功将返回执行的exeid的exename，如果SQL更新时出错则返回null
	 * @throws KPromptException
	 *             当有校验错误或其它处理错误需要返回到界面提示给用户时抛出
	 * @throws KSqlException
	 */
	public String doUpdate(String exeid, final Map<String, Object> params)
			throws KPromptException {
		try {
			return getComnDao().update(exeid, params);
		} catch (KSqlException e) {
			log.error(e.getMessage(), e);
			throw new KPromptException(e.getMessage());
		}
	}

}
