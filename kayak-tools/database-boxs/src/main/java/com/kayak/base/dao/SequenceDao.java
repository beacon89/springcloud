package com.kayak.base.dao;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kayak.base.dynamicds.CustomerContextHolder;
import com.kayak.base.exception.KPromptException;
import com.kayak.base.sql.ITransaction;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlStatement;
import com.kayak.base.sql.SqlUtil;
import com.kayak.base.sql.TResult;
import com.kayak.base.util.Tools;

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class SequenceDao extends BaseDao {

	private static Logger log = Logger.getLogger(SequenceDao.class);

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @return
	 */
	public String newId(String tablename) {
		return newId(tablename, null, 1);
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @return
	 */
	public String newId(String tablename, Integer padLeft) {
		return newId(tablename, padLeft, 1);
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @param padLeft
	 *            Integer 如果padLeft>0，则为返回的ID值左补0（padLeft指定位数）
	 * @return
	 */
	public String newId(String tablename, Integer padLeft, int step) {
		long maxid = newIdLong(tablename, step);
		String ret;
		if (padLeft != null && padLeft > 0)// 左补0
		{
			ret = Tools.padLeft(String.valueOf(maxid), padLeft, '0');
		} else {
			ret = String.valueOf(maxid);
		}
		return ret;
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @param padLeft
	 *            Integer 如果padLeft>0，则为返回的ID值左补0（padLeft指定位数）
	 * @return
	 */
	public Long newIdLong(final String tablename, final int step) {
		try {
			// 先保存原来的数据源
			String beforeDataSource = CustomerContextHolder.getCustomerType();

			TResult tr = this.doTransaction(new ITransaction() {
				@Override
				public TResult transaction(JdbcTemplate jdbcTemplate, Map<String, Object> params)
						throws KPromptException, Exception {
					String tname = tablename;
					if (Tools.strIsEmpty(tname))// 空表名称取系统自增量
						tname = "BIZR2SYSREQUENCETABLE";
					else
						tname = tname.trim().toLowerCase();
					// 先查询出sqlid对应的查询配置信息
					String sql = " UPDATE sys_sequence SET maxid=maxid+" + step + " WHERE tablename=$S{tablename} ";
					SqlStatement stm = new SqlStatement(sql, getJdbcTemplate());
					stm.setString("tablename", tname);
					int num = stm.executeUpdate();
					long maxid;
					if (num == 0) {// 没有table的sequence记录，则添加
						/* 取出表中的最大id */
						maxid = step;
						sql = " INSERT INTO sys_sequence (tablename,maxid) VALUES ($S{tablename},$N{nextID}) ";
						stm = new SqlStatement(sql, getJdbcTemplate());
						stm.setString("tablename", tname);
						stm.setLong("nextID", maxid);
						stm.executeUpdate();
					} else {
						sql = " SELECT maxid FROM sys_sequence WHERE tablename=$S{tablename} ";
						stm = new SqlStatement(sql, getJdbcTemplate());
						stm.setString("tablename", tname);
						SqlResult sr = stm.executeQuery();
						sr.next();
						maxid = sr.getLong("maxid");
					}
					return new TResult(true, null, null, maxid);
				}
			}, SqlUtil.getDataSourceSys(), null);

			// 切换回之前使用的数据源
			SqlUtil.selectDataSource(beforeDataSource);
			return (Long) tr.getObject();
		} catch (KPromptException e1) {
			log.error(e1.getMessage(), e1);
		}
		return null;
	}
}
