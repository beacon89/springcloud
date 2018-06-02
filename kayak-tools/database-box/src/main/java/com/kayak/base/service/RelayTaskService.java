package com.kayak.base.service;

import javax.annotation.Resource;

import com.kayak.base.system.AutoReloadConfigThread;

/**
 * 在这个Service中可以启停容器初始化之后要做的任务
 * 
 * @author chenhq
 * 
 */
public class RelayTaskService {

	@Resource
	private AutoReloadConfigThread autoReloadConfigThread;

	/**
	 * 初始化方法，在这里启动自动处理任务
	 */
	public void init() {
		if (autoReloadConfigThread != null) {
			autoReloadConfigThread.start();
		}
	}

	/**
	 * 在这里停止自动处理任务
	 */
	public void destroy() {
		if (autoReloadConfigThread != null) {
			autoReloadConfigThread.stopRequest();
			autoReloadConfigThread = null;
		}
	}

}
