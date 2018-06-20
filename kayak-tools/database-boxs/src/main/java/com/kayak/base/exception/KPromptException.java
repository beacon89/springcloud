package com.kayak.base.exception;

import java.util.Map;

/**
 * 需要返回提示给前端时抛出（可提供返回提示给用户）
 */
public class KPromptException extends Exception
{
	private static final long serialVersionUID = 1793500917234137967L;

	/**
	 * 返回到客户端的数据
	 */
	private Map<String, Object> returndata;

	/**
	 * 添加返回数据的构造方法
	 * 
	 * @param message
	 * @param returndata
	 *            要返回到客户端的数据JSON对象
	 */
	public KPromptException(String message, Map<String, Object> returndata)
	{
		super(message);
		this.returndata = returndata;
	}

	public KPromptException(String message)
	{
		super(message);
	}

	public Map<String, Object> getReturndata()
	{
		return returndata;
	}

	public void setReturndata(Map<String, Object> returndata)
	{
		this.returndata = returndata;
	}
}
