package com.kayak.base.system;

public interface IReloadConfig
{
	/**
	 * 触发配置文件重新加载
	 */
	public void reload();

	/**
	 * 配置文件是否初始化过，只有初始化过的配置文件才会进行定时检查更新
	 * 
	 * @return
	 */
	public boolean isInited();
}
