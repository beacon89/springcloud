package com.kayak.remote;


import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.kayak.client.UserFeignClient;
import com.kayak.controller.BeseHystrix;


@Configuration
public class UserFeignApiHystrix extends BeseHystrix implements UserFeignClient {

	private static final Logger log = LoggerFactory.getLogger(UserFeignApiHystrix.class);

	@Override
	public Map<String, Object> getUserInfoList(String json, int pageNumber, int pageSize) throws JSONException {
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> getUserInfoTotalCount() {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> getCheckUserInfo(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> UpdataUserInfo(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> getUserInfoByNamePwd(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> getUserInfoById(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> saveUserInfo(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> changeStatus(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}

	@Override
	public Map<String, Object> delUserInfo(String json) throws JSONException {
		log.error("kayak-system 服务器挂啦~~~~~~~~~~~~~~~~~~~~~~!");
		return this.returnSystemfail("kayak-system");
	}


	

}
