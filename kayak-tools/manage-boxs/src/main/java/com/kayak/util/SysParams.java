package com.kayak.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kayak.base.sql.SqlRow;
import com.kayak.base.system.SysBeans;
import com.kayak.params.dao.ParamsDao;

public class SysParams {

	private static Logger log = Logger.getLogger(SysParams.class);
	
	private static Map<String, String> parmasCache = new ConcurrentHashMap<String, String>();

	public static String getParams(String param_key) {
		
		if(!parmasCache.containsKey(param_key)){
			ParamsDao paramsDao = SysBeans.getBean("paramsDao");
			try {
				SqlRow sqlRow = paramsDao.findDictItems(param_key);
				if (sqlRow != null) {
					String param_value =  sqlRow.getString("param_value");
					parmasCache.put(param_key, param_value);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return parmasCache.get(param_key);
	
	}

}
