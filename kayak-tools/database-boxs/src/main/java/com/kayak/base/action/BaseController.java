package com.kayak.base.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.kayak.base.system.RequestSupport;

public class BaseController {

	protected static Logger log = Logger.getLogger(BaseController.class);

	/**
	 * 返回更新失败的结果
	 * 
	 * @param returnmsg
	 *            返回到客户端的文本信息（错误信息）
	 */
	protected String updateFailure(String returnmsg) {
		return updateFailure(returnmsg, null);
	}

	/**
	 * 返回更新失败的结果
	 * 
	 * @param returnmsg
	 *            返回到客户端的文本信息（错误信息）
	 * @param returndata
	 *            返回到客户端的数据JSON对象
	 */
	protected String updateFailure(String returnmsg, Map<String, Object> returndata) {
		return returnUpdate(false, returnmsg, returndata);
	}
	
	

	/**
	 * 返回更新成功的结果
	 */
	protected String updateSuccess() {
		return updateSuccess(null, null);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returndata
	 *            返回到客户端的数据JSON对象
	 */
	protected String updateSuccess(Map<String, Object> returndata) {
		return updateSuccess(null, returndata);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returnmsg
	 *            返回到客户端的文本信息
	 */
	protected String updateSuccess(String returnmsg) {
		return updateSuccess(returnmsg, null);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returnmsg
	 *            返回到客户端的文本信息
	 * @param returndata
	 *            返回到客户端的数据JSON对象
	 */
	protected String updateSuccess(String returnmsg, Map<String, Object> returndata) {
		return returnUpdate(true, returnmsg, returndata);
	}

	protected String returnUpdate(boolean success, String returnmsg, Map<String, Object> returndata) {
		return RequestSupport.updateReturnJson(success, returnmsg, returndata).toString();
	}
}
