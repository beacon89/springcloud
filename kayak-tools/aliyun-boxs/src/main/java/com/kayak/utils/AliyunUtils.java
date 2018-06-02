package com.kayak.utils;

public class AliyunUtils {
	
	public static boolean canSet(String value) {
		if(null != value && !"".equals(value)) {
			return true;
		}
		return false;
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
