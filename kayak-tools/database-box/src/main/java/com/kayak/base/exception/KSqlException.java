package com.kayak.base.exception;

/**
 * 当尝试获取SQL配置出错（多为找不到相应KEY的SQL配置）时，抛出此错误
 * 
 */
public class KSqlException extends Exception
{
	private static final long serialVersionUID = 4996236009471810186L;

	public KSqlException(String message)
	{
		super(message);
	}
}
