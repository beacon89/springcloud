package com.kayak.base.system;

import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 用于自动重新加载配置文件的线程
 * 
 * @author chenhq
 * 
 */
public class AutoReloadConfigThread extends Thread {
	private static Logger log = Logger.getLogger(AutoReloadConfigThread.class);

	private volatile boolean stopRequested;
	private Thread runThread;

	private Set<IReloadConfig> configBeans;

	/**
	 * 更新间隔时间
	 */
	private int interval = 10;

	public void run() {
		if (interval <= 0) {
			log.info("#####  自动更新配置文件服务更新间隔时间为0，将不启动");
			return;
		}
		if (interval < 5) {// 更新间隔时间不能小于5秒
			interval = 5;
		}
		log.info("##### 自动更新配置文件服务启动, 更新间隔时间 " + interval + " 秒");
		runThread = Thread.currentThread();
		stopRequested = false;
		while (!stopRequested) {
			for (IReloadConfig bean : configBeans) {
				if (bean.isInited()) {
					bean.reload();
				}
			}

			try {
				sleep(interval * 1000);
			} catch (InterruptedException e) {
				runThread.interrupt();
			}
		}
		log.info("##### 自动更新配置文件服务退出");
	}

	/**
	 * 停止线程
	 */
	public void stopRequest() {
		stopRequested = true;
		if (runThread != null)
			runThread.interrupt();
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * @param configBeans
	 *            the configBeans to set
	 */
	public void setConfigBeans(Set<IReloadConfig> configBeans) {
		this.configBeans = configBeans;
	}
}
