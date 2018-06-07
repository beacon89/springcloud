package com.kayak.controller;

import java.util.HashMap;
import java.util.Map;

public class BeseHystrix {
	
	public Map<String, Object> returnSystemfail(String systemname){
		Map<String, Object> returnmap = new HashMap<>();
		returnmap.put("returnState", TransactionCode.CODE9999);
		returnmap.put("returnMsg", "无法找到"+systemname+"服务");
		return returnmap;
	}

}
