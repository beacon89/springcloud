package com.kayak.base.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kayak.base.exception.KPromptException;
import com.kayak.base.exception.KSqlException;
import com.kayak.base.spring.TransactionResult;
import com.kayak.base.sql.ITransaction;
import com.kayak.base.sql.SqlUtil;
import com.kayak.base.sql.TResult;
import com.kayak.base.system.RequestSupportDb;
import com.kayak.base.system.SysBeans;

public class BaseDao {
	private static Logger log = Logger.getLogger(BaseDao.class);

	@Resource
	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * 在同一个事务中执行业务逻辑的方法<br />
	 * 
	 * @param transaction
	 *            ITransaction对象实例
	 * @param dataSource
	 *            指定数据源，如果值为null，则使用默认数据源
	 * @param params
	 *            参数集
	 * @return
	 * @throws KPromptException
	 */
	public TResult doTransaction(ITransaction transaction, String dataSource,
			Map<String, Object> params) throws KPromptException {
		if (transaction == null)
			return null;

		boolean donotsaylog = false;
		if (params != null && params.containsKey(RequestSupportDb.DO_NOT_SAY_LOG))
			donotsaylog = true;
		SqlUtil.selectDataSource(dataSource, donotsaylog);

		return _doTransaction(transaction, params);
	}

	/**
	 * 此函数请外部不要调用，因为需要使用@Transactional所以定义为public，只供本类内部调用使用
	 * 
	 * @throws KSqlException
	 * @throws KPromptException
	 * 
	 */
	private TResult _doTransaction(final ITransaction transaction,
			final Map<String, Object> params) throws KPromptException {
		TransactionTemplate transactionTemplate = SysBeans
				.getBean("transactionTemplate");
		final JdbcTemplate jdbc = getJdbcTemplate();
		TransactionResult tr = transactionTemplate
				.execute(new TransactionCallback<TransactionResult>() {
					@Override
					public TransactionResult doInTransaction(
							TransactionStatus status) {
						TransactionResult r = new TransactionResult();
						try {
							TResult result = transaction.transaction(jdbc,
									params);
							// if (result == null || !result.isSuccess()) {
							// throw new KSqlException(
							// "transaction return false rollback");
							// }
							r.settResult(result);
						} catch (Exception e) {
							status.setRollbackOnly();
							log.error(e.getMessage(), e);
							r.setException(e);
						}
						return r;
					}
				});
		tr.throwException();
		return tr.gettResult();
	}

}
