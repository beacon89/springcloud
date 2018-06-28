package com.kayak.util;

public class AliyunUtils {
	
	public static boolean canSet(String value) {
		if(null != value && !"".equals(value)) {
			return true;
		}
		return false;
	}
	
	public static boolean canSet(Object object) {
		if(object != null && !object.toString().equals("") && object.toString().length() > 0) {
			return true;
		}
		return false;
	}
	
	public static String canReadStr(Object object) {
		if(object != null && !object.toString().equals("") && object.toString().length() > 0 ) {
			return object.toString();
		}else {
			return "";
		}
	}
	
	public static Long canReadLong(Object object) {
		if(object != null && !object.toString().equals("") && object.toString().length() > 0) {
			return Long.valueOf(object.toString()).longValue();
		}else {
			return null;
		}
	}
	
	
	public static boolean canSet(Long value) {
		if(null != value && !"".equals(value.toString())) {
			return true;
		}
		return false;
	}
	
	public static boolean canSet(Boolean value) {
		if(null != value && !"".equals(value.toString())) {
			return true;
		}
		return false;
	}

}
