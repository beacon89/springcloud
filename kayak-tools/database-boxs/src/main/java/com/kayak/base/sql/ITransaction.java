package com.kayak.base.sql;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.kayak.base.exception.KPromptException;

public interface ITransaction
{

	/**
	 * 在同一个事务中执行的方法<br />
	 * <h1>在此方法中的代码已经是在同一个事务中执行的，事务的提交和回滚都是由ComnDao.doTransaction方法管理，
	 * 不需要在此方法中对事务进行操作，所以请不要在此方法中另外创建事务对象</h1><br />
	 * 查询的SQL语句执行例子：
	 * 
	 * <pre>
	 * // 用SQL语句构造一个SqlStatement对象
	 * SqlStatement stm = new SqlStatement(sql, session);
	 * // 使用autoSetParams方法传入params对象实现参数注入到SQL语句中
	 * stm.autoSetParams(params);
	 * // 调用executeQuery方法执行查询，得到SResult类型的结果集对象
	 * SResult result = stm.executeQuery();
	 * </pre>
	 * 
	 * 更新的SQL语句执行例子：
	 * 
	 * <pre>
	 * // 用SQL语句构造一个SqlStatement对象
	 * SqlStatement stm = new SqlStatement(sql, session);
	 * // 使用autoSetParams方法传入params对象实现参数注入到SQL语句中
	 * stm.autoSetParams(params);
	 * // 调用executeUpdate方法执行更新，得到更新影响记录数
	 * int ret = stm.executeUpdate();
	 * </pre>
	 * 
	 * @param session
	 *            {@link TSession} 当前事务创建的会话对象
	 * @return 返回一个TResult对象<br />
	 *         如果返回的TResult对象的success属性值是true，则事务正常提交<br />
	 *         如果success属性值是false，则事务回滚
	 * @throws KPromptException
	 *             如果抛出异常，事务回滚，ComnDao.doTransaction方法将返回null<br />
	 *             抛出该异常，会带给ComnDao.doTransaction，把事务回滚后，再抛出这个异常，以便返回提示信息到前端
	 * @throws Exception
	 *             如果抛出异常，事务回滚，ComnDao.doTransaction方法将返回null
	 */
	public TResult transaction(JdbcTemplate jdbcTemplate, Map<String, Object> params)
			throws KPromptException, Exception;
}
