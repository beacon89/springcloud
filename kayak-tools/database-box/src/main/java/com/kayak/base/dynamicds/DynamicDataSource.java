package com.kayak.base.dynamicds;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private static Logger log = Logger.getLogger(DynamicDataSource.class);
	protected Object determineCurrentLookupKey() {
		return CustomerContextHolder.getCustomerType();
	}

	public void close() {
	}
}
