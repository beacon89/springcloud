package com.kayak.base.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kayak.base.exception.KSqlException;
import com.kayak.base.exception.KSystemException;
import com.kayak.base.sql.SqlResult;
import com.kayak.base.sql.SqlRow;
import com.kayak.base.sql.object.SqlQueryTree;
import com.kayak.base.util.Tools;

public class KResult {
	private static Logger log = Logger.getLogger(KResult.class);

	public KResult() {
	}

	public KResult(SqlResult sr) {
		this.sResult = sr;
		this.queryTree = sr.getQueryTree();
	}

	/**
	 * 从数据库读取得到的结果集
	 */
	private SqlResult sResult;

	protected SqlQueryTree queryTree;
	protected boolean checkbox;
	/**
	 * 从客户端传来的初始展开状态，如果有传这个参数，则忽略queryTree的InitExpand
	 */
	protected String initExpand;

	/**
	 * 从客户端传来的，分支节点是否单击展开的设置
	 */
	protected boolean singleClickExpand = true;

	/**
	 * 返回树结构的结果集JSON对象
	 * 
	 * @param queryTree
	 *            树配置信息
	 * @param checkbox
	 *            是否显示复选框
	 * @return
	 * @throws KSystemException
	 */
	public JSONArray getTreeJson() throws KSystemException {
		// 要返回树结构结果返回，先要把结果集构造成树结构
		makeTreeResult();

		return this.getTreeJson(this.sResult);
	}

	/**
	 * 是否已构造过树结构结果集
	 */
	private boolean makedTreeResult = false;

	/**
	 * 把查询结果集构造成树结构结果集
	 * 
	 * @throws KSystemException
	 */
	public void makeTreeResult() throws KSystemException {
		if (this.makedTreeResult)// 已构造了树结构结果集，不必再处理
			return;

		if (queryTree == null)
			throw new KSystemException("构造树结构结果集失败：SQL配置的树配置(queryTree)未设置");

		if (this.sResult == null || this.sResult.getRowSize() == 0) {
			log.info("构造树结构结果集：查询无记录");
			return;
		}

		// if (!this.queryTree.isAsync())//非异步加载，把结果集构造成树结构
		{
			if ("pos".equalsIgnoreCase(this.queryTree.getDiffway())) {
				dealPos(this.queryTree.getDiffcondition().trim());
			} else if ("upper".equalsIgnoreCase(this.queryTree.getDiffway())) {
				String[] diffs = this.queryTree.getDiffcondition().split("[,]");
				if (diffs.length != 2) {
					throw new KSystemException(
							"构造树结果集时，diffCondition设置的条件内容不正确");
				}
				dealUpper(diffs[0].trim(), diffs[1].trim());
			} else {
				throw new KSystemException("树配置【分级方式】(diffWay)错误");
			}
			this.makedTreeResult = true;
		}
	}

	/**
	 * 处理分级方式为'P'的构造
	 * 
	 * @param columnName
	 *            分级字段名称
	 * @throws KSystemException
	 */
	private void dealPos(String columnName) throws KSystemException {
		String[] columnNames = columnName.split("[,]");
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columnNames[i].trim();
			if (!this.sResult.hasColumn(columnNames[i])) {
				throw new KSystemException("构造树结果集时，diffCondition指定的字段名称 "
						+ columnNames[i] + " 不存在");
			}
		}
		List<SqlRow> removeRows = new ArrayList<SqlRow>();
		int rowsize = this.sResult.getRowSize();
		for (int line = 0; line < rowsize; line++) {
			try {
				SqlRow currRow = this.sResult.getRow(line);
				StringBuilder sb = new StringBuilder();
				for (String cname : columnNames) {
					sb.append(currRow.getString(cname));
				}
				String value = sb.toString();

				while (!Tools.strIsEmpty(value)) {// value每删去最后一位字符，遍历查找一次上级
					value = Tools.substr(value, 0, value.length() - 1);
					for (int i = line - 1; i != line; i--) {// 从第line - 1
															// 行开始遍历，直到第line行退出
						if (i < 0) {// 遍历到第一行后，再从最后一行开始遍历
							i = rowsize - 1;
							if (i == line)
								break;
						}
						try {
							SqlRow row = this.sResult.getRow(i);
							sb = new StringBuilder();
							for (String cname : columnNames) {
								sb.append(row.getString(cname));
							}
							String val = sb.toString();
							if (val.equals(value)) {// 找到上级
								row.addChild(currRow);// 将该行添加到上级的childrens中
								removeRows.add(currRow);// 保存要从sqlResult中移去的行，将在后面统一移去
								break;
							}
						} catch (KSqlException e) {
							log.error(e.getMessage(), e);
						}
					}
					// 已找到上级，退出value遍历
					if (removeRows.contains(currRow))
						break;
				}
			} catch (KSqlException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
		updateSqlResult(removeRows);
	}

	/**
	 * 处理分级方式为'U'的构造
	 * 
	 * @param baseColumnName
	 *            基准字段名称
	 * @param upperColumnName
	 *            上级字段名称
	 * @throws KSystemException
	 */
	private void dealUpper(String baseColumnName, String upperColumnName)
			throws KSystemException {
		if (!this.sResult.hasColumn(baseColumnName)) {
			throw new KSystemException("构造树结果集时，diffCondition指定的基准字段名称 "
					+ baseColumnName + " 不存在");
		}
		if (!this.sResult.hasColumn(upperColumnName)) {
			throw new KSystemException("构造树结果集时，diffCondition指定的上级字段名称 "
					+ upperColumnName + " 不存在");
		}
		List<SqlRow> removeRows = new ArrayList<SqlRow>();
		for (int line = 0; line < this.sResult.getRowSize(); line++) {
			try {
				SqlRow currRow = this.sResult.getRow(line);
				String upperValue = currRow.getString(upperColumnName);
				for (int i = 0; i < this.sResult.getRowSize(); i++) {// 从遍历查找上级
					try {
						SqlRow row = this.sResult.getRow(i);
						String baseValue = row.getString(baseColumnName);
						if (baseValue.equals(upperValue)) {// 找到上级
							row.addChild(currRow);// 将该行添加到上级的childrens中
							removeRows.add(currRow);// 保存要从sqlResult中移去的行，将在后面统一移去
							break;
						}
					} catch (KSqlException e) {
						log.error(e.getMessage(), e);
					}
				}
			} catch (KSqlException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
		updateSqlResult(removeRows);
	}

	/**
	 * 经过dealPos或dealUpper构造树结构结果集后，需要对sqlResult的每行进行更新，主要是以下操作：<br />
	 * 1. 设置没有子节点的行为叶子节点<br />
	 * 2. 从sqlResult中移去已被归属到其它节点的子集中的行
	 * 
	 * @param removeRows
	 */
	private void updateSqlResult(List<SqlRow> removeRows) {
		for (int i = 0; i < this.sResult.getRowSize(); i++) {
			try {
				SqlRow row = this.sResult.getRow(i);
				if (!row.hasChilds()) {// 没有子项的节点设置为叶子节点
					row.setLeaf(true);
				}
				if (removeRows.contains(row)) {// 从sqlResult移走需要移去的行
					this.sResult.removeRow(row);
					i--;
				}
			} catch (KSqlException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 构造树结查的结果集JSON对象
	 * 
	 * @param sr
	 *            用于构造的结果集
	 * @return
	 * @throws KSystemException
	 */
	private JSONArray getTreeJson(SqlResult sr) {
		if (sr == null) {
			log.info("查询结果集sResult=null");
			return null;
		}

		if (sr.getRowSize() == 0) {// 查无记录，返回空结果
			return new JSONArray();
		}

		sr.resetCursor();

		JSONArray jsonArr = new JSONArray();

		while (sr.next()) {
			try {
				SqlRow row = sr.getRow();

				JSONObject jo = new JSONObject();
				jsonArr.put(jo);
				// 把记录行的所有字段添加到节点的rowData属性中
				jo.put("rowData", row);
				// 添加树配置属性信息
				if (!Tools.strIsEmpty(queryTree.getIdcolumn())) {
					// 如果指定多个字段，则把多列值用"-"连接起来
					String[] columns = queryTree.getIdcolumn().split("[,]");
					StringBuilder sb = new StringBuilder();
					for (String cname : columns) {
						if (sb.length() > 0)
							sb.append("-");
						sb.append(row.getString(cname.trim()));
					}
					String value = sb.toString();
					jo.put("id", value);
					jo.put("value", value);
				}
				String text;
				if (!Tools.strIsEmpty(queryTree.getTextcolumn())) {
					// 如果指定多个字段，则把多列值用空格连接起来
					StringBuilder sb = new StringBuilder();
					String[] columns = queryTree.getTextcolumn().split("[,]");
					for (String cname : columns) {
						String val = row.getString(cname.trim());
						if (Tools.strIsEmpty(val))
							continue;

						if (sb.length() > 0)
							sb.append("-");
						sb.append(val);
					}
					text = sb.toString();
					jo.put("text", text);
				} else {
					jo.put("text", "");
				}
				jo.put("iconCls", row.getString(queryTree.getIconClscolumn()));
				jo.put("icon", row.getString(queryTree.getIconcolumn()));
				if (checkbox) {// 显示复选框
					jo.put("checked", false);
				}
				if (row.isLeaf() && !queryTree.getIsasync()) {// 是叶子节点，无子节点，并且不是异步加载模式的
					jo.put("leaf", true);
				} else {
					if (this.singleClickExpand)
						jo.put("singleClickExpand", true);
					// 初始展开方式
					boolean expanded = false;
					if (!queryTree.getIsasync()
							&& (// 非异步加载模式时，确定树初始展开情况
							// 先判断initExpand
							"all".equalsIgnoreCase(this.initExpand)
									|| ("one".equalsIgnoreCase(this.initExpand) && row
											.isFirst())
							// 如果initExpand==null则使用queryTree的配置
							|| (this.initExpand == null
									&& "all".equalsIgnoreCase(queryTree
											.getInitexpand()) || "one"
									.equalsIgnoreCase(queryTree.getInitexpand())
									&& row.isFirst()))) {// 全部展开，或只展表第一个节点
						expanded = true;
					}
					jo.put("expanded", expanded);
					if (row.getChildrens() != null)
						jo.put("children", getTreeJson(row.getChildrens()));
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return jsonArr;
	}

	/**
	 * 返回数组结构的结果集JSON对象
	 * 
	 * @return
	 * @throws KSystemException
	 */
	public JSONArray getArrayJson() {
		if (this.sResult == null) {
			log.info("查询结果集sResult=null");
			return null;
		}

		this.sResult.resetCursor();

		JSONArray array = new JSONArray();
		try {
			while (sResult.next()) {
				JSONArray arr = new JSONArray(sResult.getRow().values());
				array.put(arr);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return array;
	}

	/**
	 * 返回列表结构的结果集JSON对象
	 * 
	 * @return
	 * @throws KSystemException
	 */
	public JSONObject getListJson() {
		if (this.sResult == null) {
			log.info("查询结果集sResult=null");
			return null;
		}

		this.sResult.resetCursor();

		JSONObject json = new JSONObject();
		try {
			json.put("results", this.sResult.getCount());

			JSONArray rowArr = new JSONArray();
			json.put("rows", rowArr);

			while (sResult.next()) {
				JSONObject jo = new JSONObject(sResult.getRow());
				rowArr.put(jo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return json;
	}

	public Map<String, Object> getListMap() {
		if (this.sResult == null) {
			log.info("查询结果集sResult=null");
			return null;
		}

		this.sResult.resetCursor();

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("results", this.sResult.getCount());

			List<SqlRow> list = new ArrayList<SqlRow>();
			map.put("rows", list);

			while (sResult.next()) {
				list.add(sResult.getRow());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

	public SqlResult getSResult() {
		return sResult;
	}

	public void setSResult(SqlResult sResult) {
		this.sResult = sResult;
	}

	public SqlQueryTree getQueryTree() {
		return queryTree;
	}

	public void setQueryTree(SqlQueryTree queryTree) {
		this.queryTree = queryTree;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public String getInitExpand() {
		return initExpand;
	}

	public void setInitExpand(String initExpand) {
		this.initExpand = initExpand;
	}

	public Boolean getSingleClickExpand() {
		return singleClickExpand;
	}

	public void setSingleClickExpand(Boolean singleClickExpand) {
		this.singleClickExpand = singleClickExpand;
	}

	public boolean isMakedTreeResult() {
		return makedTreeResult;
	}

	public void setSingleClickExpand(boolean singleClickExpand) {
		this.singleClickExpand = singleClickExpand;
	}

	public void setMakedTreeResult(boolean makedTreeResult) {
		this.makedTreeResult = makedTreeResult;
	}

}
