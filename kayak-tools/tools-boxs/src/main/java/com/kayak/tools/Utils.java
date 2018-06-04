package com.kayak.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;



public class Utils {
	
	public static final DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
	
	public static String getUUid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getThisData() {
		DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		return dfDate.format(new Date());
	}
	

}
