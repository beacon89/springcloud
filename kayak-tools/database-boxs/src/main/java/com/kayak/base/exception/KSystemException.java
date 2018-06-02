package com.kayak.base.exception;

/**
 * 系统级程序产生错误时抛出
 */
public class KSystemException extends Exception
{
	private static final long serialVersionUID = -5567022272108120638L;

	public KSystemException(String message)
	{
		super(message);
	}
}
