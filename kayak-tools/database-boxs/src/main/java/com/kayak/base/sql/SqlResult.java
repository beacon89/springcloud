package com.kayak.base.sql;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.kayak.base.exception.KSqlException;
import com.kayak.base.sql.object.SqlQueryTree;
import com.kayak.base.sql.object.SqlTransfer;

public class SqlResult {
	protected static Logger log = Logger.getLogger(SqlResult.class);

	private List<SqlTransfer> transfers;

	private SqlQueryTree queryTree;

	/**
	 * 执行查询后构造出来的结果集
	 */
	private List<SqlRow> rows;

	/**
	 * 总记录数
	 */
	private long count = 0;

	/**
	 * 当前行<br />
	 * 用next()方法下移行，prev()方法上移行
	 */
	private int currLine = -1;

	/**
	 * 查询出来的所有列名称
	 */
	private List<String> columns;

	public SqlResult(ResultSet result) throws SQLException {
		rows = new ArrayList<SqlRow>();// 结果记录集对象
		columns = new ArrayList<String>();// 字段集对象

		if (result == null)
			return;

		// 字段信息
		ResultSetMetaData md = result.getMetaData();
		int line = 0;
		SqlRow last = null;
		while (result.next()) {
			SqlRow m = new SqlRow();
			m.setFirst(line == 0);
			for (int i = 1; i <= md.getColumnCount(); i++) {
				// 所有字段名称以小写形式保存
				String columnName = md.getColumnLabel(i).toLowerCase();
				Object value = null;
				int type = md.getColumnType(i);
				switch (type) {
				case Types.CLOB:
					try {
						Clob clob = result.getClob(i);
						if (clob != null) {
							value = IOUtils.toString(clob.getCharacterStream());
						}
					} catch (IOException e) {
						value = null;
						log.error(e.getMessage(), e);
					}
					break;
				case Types.BLOB:
					try {
						Blob blob = result.getBlob(i);
						if (blob != null) {
							value = IOUtils.toByteArray(blob.getBinaryStream());
						}
					} catch (IOException e) {
						value = null;
						log.error(e.getMessage(), e);
					}
					break;
				case Types.BIGINT:
					value = result.getLong(i);
					break;
				case Types.INTEGER:
					value = result.getInt(i);
					break;
				case Types.DOUBLE:
					value = result.getDouble(i);
					break;
				case Types.FLOAT:
					value = result.getFloat(i);
					break;
				case Types.DECIMAL:
				case Types.NUMERIC:
					if (result.getObject(i) == null) {
						value = null;
					} else {
						int scale = md.getScale(i);
						if (scale > 0) {
							value = result.getDouble(i);
						} else if (md.getPrecision(i) == 0 && scale == -127) {// sum(),
																				// max()等这种聚合函数或子查询出来的非实际表字段的值
							value = result.getDouble(i);
						} else {
							Double d = result.getDouble(i);
							Long l = result.getLong(i);

							double dAbs = Math.abs(d);
							double lAbs = Math.abs(l);
							if (dAbs > lAbs) {
								value = d;
							} else {
								value = l;
							}
						}
					}
					break;
				default:
					value = result.getObject(i);
				}
				// 添加row记录
				m.put(columnName, value);
				// 收集字段名
				if (!columns.contains(columnName))
					columns.add(columnName);
			}
			rows.add(m);
			line++;

			m.setLast(false);
			last = m;
		}
		if (last != null) {// 只把最后一条记录设置last是true
			last.setLast(true);
		}
	}

	/**
	 * 使用List<Map<String,Object>>创建结果集数据
	 * 
	 * @throws SQLException
	 */
	public SqlResult(Collection<Map<String, Object>> maps) {
		rows = new ArrayList<SqlRow>();// 结果记录集对象
		columns = new ArrayList<String>();// 字段集对象

		if (maps == null)
			return;

		// 字段信息
		for (Map<String, Object> map : maps) {
			SqlRow row = new SqlRow();
			Set<Map.Entry<String, Object>> set = (Set<Map.Entry<String, Object>>) map
					.entrySet();
			for (Map.Entry<String, Object> e : set) {
				String columnName = e.getKey();
				Object value = e.getValue();
				// 添加row记录
				row.put(columnName, value);
				// 收集字段名
				if (!columns.contains(columnName))
					columns.add(columnName);
			}
			rows.add(row);
		}
		this.count = rows.size();
	}

	public SqlResult() {// 空结果集构造函数
		rows = new ArrayList<SqlRow>();// 结果记录集对象
		columns = new ArrayList<String>();// 字段集对象
	}

	/**
	 * 结果集是否存在指定字段名称
	 */
	public boolean hasColumn(String columnName) {
		// 所有字段名称以大写形式保存
		columnName = columnName.toLowerCase();
		return columns.contains(columnName);
	}

	public void addColumn(String columnName) {
		// 所有字段名称以大写形式保存
		columnName = columnName.toLowerCase();
		if (!columns.contains(columnName))
			columns.add(columnName);
	}

	/**
	 * 当前记录游标指向下一条记录
	 * 
	 * @return 如果当前游标已指向最后一条记录，调用next()则返回false，否则返回true
	 */
	public boolean next() {
		if (this.getRowSize() == 0)
			return false;

		if (currLine + 1 >= this.getRowSize()) {
			return false;
		} else {
			currLine++;
			return true;
		}
	}

	/**
	 * 当前记录游标指向上一条记录
	 * 
	 * @return 如果当前游标已指向第一条记录，调用prev()则返回false，否则返回true
	 */
	public boolean prev() {
		if (this.getRowSize() == 0)
			return false;

		if (currLine <= 0) {
			return false;
		} else {
			currLine--;
			return true;
		}
	}

	/**
	 * 重置记录游标<br />
	 * 这样就可以使用游标重头开始遍历结果集的记录了
	 */
	public void resetCursor() {
		this.currLine = -1;
	}

	/**
	 * 取得查询结果当前游标所在的记录
	 * 
	 * @throws KSqlException
	 */
	public SqlRow getRow() throws KSqlException {
		if (rows == null)
			throw new KSqlException("查询结果为空");

		if (currLine == -1)
			throw new KSqlException("记录游标未指向任何记录");

		return this.rows.get(currLine);
	}

	/**
	 * 取得查询结果第line条记录<br />
	 * 此方法不会改变当前游标的值
	 */
	public SqlRow getRow(int line) throws KSqlException {
		if (rows == null)
			throw new KSqlException("查询结果为空");

		if (rows.size() < line)
			throw new KSqlException("查询结果没有第" + line + "条记录");

		return this.rows.get(line);
	}

	/**
	 * 取得查询结果（分页结果）记录数
	 * 
	 * @return 返回查询结果记录数，如果有分页，则返回的是当前页记录数
	 */
	public int getRowSize() {
		return this.rows.size();
	}

	/**
	 * 取得当前游标行的String字段值
	 * 
	 * @throws KSqlException
	 */
	public String getString(String key) throws KSqlException {
		return this.getRow().getString(key);
	}

	public Object getObject(String key) throws KSqlException {
		return this.getRow().get(key);
	}

	/**
	 * 取得当前游标行的Integer字段值
	 */
	public Integer getInteger(String key) throws KSqlException {
		return this.getRow().getInteger(key);
	}

	/**
	 * 取得当前游标行的Short字段值
	 */
	public Short getShort(String key) throws KSqlException {
		return this.getRow().getShort(key);
	}

	/**
	 * 取得当前游标行的Long字段值
	 */
	public Long getLong(String key) throws KSqlException {
		return this.getRow().getLong(key);
	}

	/**
	 * 取得当前游标行的Double字段值
	 */
	public Double getDouble(String key) throws KSqlException {
		return this.getRow().getDouble(key);
	}

	/**
	 * 取得当前游标行的BigDecimal字段值
	 */
	public BigDecimal getBigDecimal(String key) throws KSqlException {
		return this.getRow().getBigDecimal(key);
	}

	public Object getByColumnIndex(int index) throws KSqlException {
		return this.getRow().get(this.columns.get(index));
	}

	public byte[] getByteArray(String key) throws KSqlException {
		return this.getRow().getByteArray(key);
	}

	/**
	 * 取得查询结果记录总数
	 */
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<String> getColumns() {
		return columns;
	}

	public List<SqlRow> getRows() {
		return rows;
	}

	/**
	 * 添加行
	 * 
	 * @param row
	 */
	public void addRow(SqlRow row) {
		this.rows.add(row);
		for (String key : row.keySet()) {
			String columnName = key.toLowerCase();
			if (!this.columns.contains(columnName))
				this.columns.add(columnName);
		}
		this.count++;
	}

	public void addResult(SqlResult result) {
		if (result == null || result.getRowSize() == 0)
			return;

		result.resetCursor();
		while (result.next()) {
			try {
				this.addRow(result.getRow());
			} catch (KSqlException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 删除行
	 * 
	 * @param row
	 */
	public void removeRow(SqlRow row) {
		this.rows.remove(row);
		this.count--;
	}

	/**
	 * 取得当前游标行
	 * 
	 * @return
	 */
	public int getCurrLine() {
		return currLine;
	}

	public static void main(String[] args) {
		Double d = 1.0000;
		Long l = 1l;
		System.out.println(d > l);
	}

	public SqlQueryTree getQueryTree() {
		return queryTree;
	}

	public void setQueryTree(SqlQueryTree queryTree) {
		this.queryTree = queryTree;
	}

	public List<SqlTransfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<SqlTransfer> transfers) {
		this.transfers = transfers;
	}
}
