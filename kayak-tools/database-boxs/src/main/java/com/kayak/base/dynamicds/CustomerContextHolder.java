package com.kayak.base.dynamicds;

import org.apache.log4j.Logger;

public class CustomerContextHolder
{
	private static Logger log = Logger.getLogger(CustomerContextHolder.class);

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerType(String customerType)
	{
		setCustomerType(customerType, false);
	}

	public static void setCustomerType(String customerType, boolean donotsaylog)
	{
		if (!donotsaylog)
			log.info("##### using data source : " + customerType);
		contextHolder.set(customerType);
	}

	public static String getCustomerType()
	{
		return contextHolder.get();
	}

	public static void clearCustomerType()
	{
		contextHolder.remove();
	}
}
