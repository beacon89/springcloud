package com.kayak.base.sql;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlRow extends LinkedHashMap<String, Object>
{
	private static final long serialVersionUID = -6949629281253429688L;

	/**
	 * 用于树状结果集，保存子集
	 */
	private SqlResult childrens;

	/**
	 * 是否叶子节点
	 */
	private boolean isLeaf;

	/**
	 * 是否第一条记录
	 */
	private boolean isFirst;
	
	/**
	 * 是否最后一条记录
	 */
	private boolean isLast;

	public Object get(Object key)
	{
		if (key == null)
			return null;

		return super.get(key.toString().toLowerCase());
	}

	public Object put(String key, Object value)
	{
		if (key == null)
			return null;
		return super.put(key.toLowerCase(), value);
	}

	public void putAll(Map<? extends String, ? extends Object> m)
	{
		for (Iterator<? extends Map.Entry<? extends String, ? extends Object>> i = m.entrySet()
				.iterator(); i.hasNext();)
		{
			Map.Entry<? extends String, ? extends Object> e = i.next();
			put(e.getKey(), e.getValue());
		}
		// super.putAll(m);
	}

	/**
	 * 取得String字段值
	 */
	public String getString(String key)
	{
		return SqlUtil.obj2String(this.get(key));
	}

	/**
	 * 取得Integer字段值
	 */
	public Integer getInteger(String key)
	{
		return SqlUtil.obj2Integer(this.get(key));
	}

	/**
	 * 取得Short字段值
	 */
	public Short getShort(String key)
	{
		return SqlUtil.obj2Short(this.get(key));
	}

	/**
	 * 取得Long字段值
	 */
	public Long getLong(String key)
	{
		return SqlUtil.obj2Long(this.get(key));
	}

	/**
	 * 取得Double字段值
	 */
	public Double getDouble(String key)
	{
		return SqlUtil.obj2Double(this.get(key));
	}

	/**
	 * 取得BigDecimal字段值
	 */
	public BigDecimal getBigDecimal(String key)
	{
		return SqlUtil.obj2BigDecimal(this.get(key));
	}

	/**
	 * 取得Blob字段值
	 */
	public byte[] getByteArray(String key)
	{
		return SqlUtil.obj2ByteArray(this.get(key));
	}

	/**
	 * 设置是否叶子节点
	 */
	public void setLeaf(boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	/**
	 * 返回是否叶子节点
	 */
	public boolean isLeaf()
	{
		return this.isLeaf && !this.hasChilds();
	}

	/**
	 * 取得子集
	 */
	public SqlResult getChildrens()
	{
		return childrens;
	}

	/**
	 * 设置子集
	 * 
	 * @param childrens
	 */
	public void setChildrens(SqlResult childrens)
	{
		this.childrens = childrens;
	}

	/**
	 * 返回本记录是否有子集
	 */
	public boolean hasChilds()
	{
		return this.childrens != null && this.childrens.getRowSize() > 0;
	}

	public boolean isFirst()
	{
		return isFirst;
	}

	public void setFirst(boolean isFirst)
	{
		this.isFirst = isFirst;
	}

	public void addChild(SqlRow row)
	{
		if (this.childrens == null)
		{
			this.childrens = new SqlResult();
		}
		this.childrens.addRow(row);
	}

	/**
	 * @return the isLast
	 */
	public boolean isLast()
	{
		return isLast;
	}

	/**
	 * @param isLast the isLast to set
	 */
	public void setLast(boolean isLast)
	{
		this.isLast = isLast;
	}

}
